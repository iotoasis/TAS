package drools;

public class BusinessTrip {

    private String name;
    private double hour ;
   
    public BusinessTrip(String name, double hour){
      this. name = name;
      this. hour  = hour;
    }
     
    public String getName(){
      return name;
    }
   
    public double getHour(){
      return hour;
    }
}


