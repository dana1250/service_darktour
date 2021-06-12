package darktour;


/**
 *
 * Member에 해당하는 정보에 대한 저장소 역할
 *
 */
public class HistoricVO {
/**
 * 필요한 property 선언
 */
    private int HISTORIC_NUM;
    private String NAME;
    private String EXPLAIN_HIS;
    private String ADDRESS;

   
    public HistoricVO(){}
    public HistoricVO(int HISTORIC_NUM, String NAME, String EXPLAIN_HIS, String ADDRESS){
       
        this.HISTORIC_NUM = HISTORIC_NUM;
        this.NAME = NAME;
        this.EXPLAIN_HIS = EXPLAIN_HIS;
        this.ADDRESS = ADDRESS;       
    }
	public int getHISTORIC_NUM() {
		return HISTORIC_NUM;
	}
	public void setHISTORIC_NUM(int hISTORIC_NUM) {
		HISTORIC_NUM = hISTORIC_NUM;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getEXPLAIN_HIS() {
		return EXPLAIN_HIS;
	}
	public void setEXPLAIN_HIS(String eXPLAIN_HIS) {
		EXPLAIN_HIS = eXPLAIN_HIS;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	} 
    
}
