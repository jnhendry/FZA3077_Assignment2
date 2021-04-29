package com.hotmail.jamesnhendry.fza3077;

public class MedicalRecord {
    String dateUpdated, patientUsername;
    private int age;
    private double bloodpressure,reynoldsRiskScore,cReactive,apolprotB,apolprotA,lipProteinA;
    private boolean smoker;
    private boolean famhist;
    private Visit visit;

    public MedicalRecord(Visit visit) {
        this.visit = visit;
        bloodpressure = visit.getBloodpressure();
         cReactive = visit.getcReactive();
         apolprotB = visit.getApolprotB();
         apolprotA = visit.getApolprotA();
         lipProteinA = visit.getLipProteinA();
        smoker = visit.isSmoker();
        famhist = visit.isFamhist();
        dateUpdated = visit.getDate();
        patientUsername = visit.getPatientUsername();
        age = visit.getAge();
    }

    public double calculateReynoldsRiskScore(){
        double A = (((0.0785)*(age)+(3.271)*Math.log(bloodpressure)+(0.202)*Math.log(cReactive)+(0.00820)*(apolprotB)));
        A=A-(0.00769)*(apolprotA+0.134);
        if(smoker){
            A=A+0.825;
        }
        if(famhist){
            A=A+0.427;
        }
        if(lipProteinA>10&&apolprotB>=100){
            A=A+0.00742*lipProteinA;
        }
        reynoldsRiskScore = (1-Math.pow(0.98756,A-19.848))*100;
        return reynoldsRiskScore;
    }
}
