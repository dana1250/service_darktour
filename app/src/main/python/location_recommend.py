import pandas as pd
from os.path import dirname,join
from sklearn.feature_extraction.text import TfidfVectorizer
import numpy as np
import re
from sklearn.metrics.pairwise import cosine_similarity

filename = join(dirname(__file__),"pre_darktour.csv")# csv 읽어오기
ai_process = pd.read_csv(filename)

pre_explain = ai_process['explain'] #유적지 설명
pre_num = ai_process['num'] #유적지 번호
pre_name = ai_process['name'] #유적지 이름
pre_address = ai_process['address']


def tfidf_fn(search_history_name,location):
    tfidf = TfidfVectorizer(max_features=500,ngram_range = (1,2),min_df=1) # tfidf 벡터화    
    tfidf_matrix = tfidf.fit_transform(pre_explain) #tfidf 벡터값 메트리스 생성
    cosine_simillar = cosine_fn(search_history_name,location,tfidf_matrix)
    return cosine_simillar

def cosine_fn(search_history_name,location,tfidf_matrix):
    cosine_matrix = cosine_similarity(tfidf_matrix, tfidf_matrix)

    #유적지이름과 id를 매핑할 dictionary를 생성해줍니다. 
    histroy_name = {}
    sim_scores=[]
    for i, c in enumerate(pre_name): histroy_name[i] = c

    # id와 movie title를 매핑할 dictionary를 생성해줍니다. 
    id_name = {}
    for i, c in histroy_name.items(): id_name[c] = i

    # 관심 유적지의 id 추출 
    favorite_search_history = search_history_name.split(',')
    favorite_search_history.pop() #제일 마지막 , 제거
    
    # 유적지 주소와 id를 매핑할 dictionary를 생성해줍니다. 
    addressid = {}
    favorite_address_site = [] # 관심 유적지와 같은 유적지
    for i, c in enumerate(pre_address): 
        addressid[i] = c[0:2]
        if histroy_name[i] in favorite_search_history and c[0:2] == location:
            favorite_address_site.append(histroy_name[i]); # 유적지 주소와 맞는 dictionary(유적지이름)

    for j,search in enumerate(favorite_search_history):
        idx = id_name[search] 
        sim_scores.insert(j,[(i, c) for i, c in enumerate(cosine_matrix[idx]) if i != idx]) # 자기 자신을 제외한 영화들의 유사도 및 인덱스를 추출 
        sim_scores.insert(j,sorted(sim_scores[j], key = lambda x: x[1], reverse=True)) # 유사도가 높은 순서대로 정렬
    
    final_dict = {} # 임시 최종 출력
    final = []
    address_scores = []
    for j in range(len(favorite_search_history)):
        address_scores.insert(j,[(addressid[i], score,histroy_name[i]) for i, score in sim_scores[j]])
        location_search = []
        final_array = []
        count = 0
        for x in address_scores[j]:
            if x[0] == location and count < 15:
                location_search.append(x[1:3])
                count += 1
        final_dict[j] = location_search
    
    final = pd.DataFrame(final_dict).T
    pre_check = pre_checkfn(final,favorite_search_history,favorite_address_site)
    return pre_check
    
def pre_checkfn(final,favorite_search_history,favorite_address_site):
    change_list = []
    temp = []
    for item in favorite_address_site:
        change_list.append(item)
    j = 0
    while True:
        if j < 2: #초기에 값 유적지 3개면 6개 들어가게
            for data in final[j]:
                change_list.append(data[1])
                
            
        else: # 유적지 초기 이후 추가로
            change_list,check = check_finish(change_list,len(favorite_search_history),j-1,final,temp,favorite_address_site)
           
            if check: #만족

                return "-".join(map(str, change_list)) # return 
            
            else: #만족 x
                
                for count in range(len(final[j])):
                    change_list.append(final[j][count][1]) #다음 줄의 유적지들 추가
                    temp.append(final[j][count])
                change_list = set(change_list) #list 중복 제거
        j+=1               
        
def check_finish(list_,size,j,final,temp,favorite_address_site): #만족 체크
    check_list = list(set(list_))
    check_list_size = len(check_list)
    if check_list_size == size*3: #만족하면 true
        return list(check_list),True
    elif check_list_size < size*3: # 개수보다 작을 때
        return list(check_list),False
    else: #개수보다 클때
        for count in range(len(final[j])):
            temp.append(final[j][count]) #다음 줄의 유적지들 추가
        temp = sorted(list(set(temp)), key = lambda x: x[0], reverse=False) #정렬
        
        for i in range(check_list_size - size*3): #제거된 값들 채워넣기
            try:
                if temp[i][1] not in favorite_address_site:
                    check_list.remove(temp[i][1])
            except ValueError:
                pass
        return list(set(check_list)),True

def main(search_history_name,location):
    
    recommend_site = tfidf_fn(search_history_name,location)
    return recommend_site

