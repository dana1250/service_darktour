#create function


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


def tfidf_fn(search_history_name):
    tfidf = TfidfVectorizer(max_features=500,ngram_range = (1,2),min_df=1) # tfidf 벡터화    
    tfidf_matrix = tfidf.fit_transform(pre_explain) #tfidf 벡터값 메트리스 생성
    cosine_simillar = cosine_fn(search_history_name,tfidf_matrix)
    return cosine_simillar

def cosine_fn(search_history_name,tfidf_matrix):
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
    favorite_search_history.pop() #마지막 콤마 제거
    for j,search in enumerate(favorite_search_history):
        idx = id_name[search] 
        sim_scores.insert(j,[(i, c) for i, c in enumerate(cosine_matrix[idx]) if i != idx]) # 자기 자신을 제외한 영화들의 유사도 및 인덱스를 추출 
        sim_scores.insert(j,sorted(sim_scores[j], key = lambda x: x[1], reverse=True)) # 유사도가 높은 순서대로 정렬
    
    final_dict = {} # 임시 최종 출력
    final = []
    scores = []
    for j in range(len(favorite_search_history)):
        scores.insert(j,[(score,histroy_name[i]) for i, score in sim_scores[j]])
        every_search = []
        final_array = []
        count = 0
        for x in scores[j]:
            if count < 20:
                every_search.append(x[0:2])
                count += 1
        final_dict[j] = every_search

    final = pd.DataFrame(final_dict).T
    pre_check = pre_checkfn(final,favorite_search_history)
    return pre_check
    
def pre_checkfn(final,favorite_search_history):
    change_list = []
    temp = []
    for item in favorite_search_history:
        change_list.append(item)
    j = 0
    while True:
        if j < 3: #초기에 값 유적지 3개면 9개 들어가게
            for data in final[j]:
                change_list.append(data[1])
        else: # 유적지 초기 이후 추가로
            change_list,check = check_finish(change_list,len(favorite_search_history),j-1,final,temp,favorite_search_history)
           # print(change_list)
            if check: #만족

                return "-".join(map(str, change_list)) # return 
            
            else: #만족 x
                
                for count in range(len(final[j])):
                    change_list.append(final[j][count][1]) #다음 줄의 유적지들 추가
                    temp.append(final[j][count])
                change_list = set(change_list) #list 중복 제거
        j += 1
        
def check_finish(list_,size,j,final,temp,favorite_search_history): #만족 체크
    check_list = list(set(list_))
    check_list_size = len(check_list)
    if check_list_size == size*4: #만족하면 true
        return list(check_list),True
    elif check_list_size < size*4: # 개수보다 작을 때
        return list(check_list),False
    else: #개수보다 클때
        for count in range(len(final[j])):
            temp.append(final[j][count]) #다음 줄의 유적지들 추가
        temp = sorted(list(set(temp)), key = lambda x: x[0], reverse=False) #정렬
        for i in range(check_list_size - size*4): #제거된 값들 채워넣기
            try:
                if temp[i][1] not in favorite_search_history:
                    check_list.remove(temp[i][1])
            except ValueError:
                pass
        return list(set(check_list)),True

def main(search_history_name):
    
    recommend_site = tfidf_fn(search_history_name)
    
    return recommend_site

    
    
