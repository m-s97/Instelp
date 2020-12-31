package Test;

import java.util.List;

//import com.sun.xml.internal.txw2.annotation.XmlElement;

public class JsonObject {
	 public String placeName;
     public String placeLat;
     public String placeLong;
     public String placeLocation;
     public String flag;
     public List<MenuJson> menu;
     public String cust_lat;
     public String cust_long;
     
     
	public String getCust_lat() {
		return cust_lat;
	}
	public void setCust_lat(String cust_lat) {
		this.cust_lat = cust_lat;
	}
	public String getCust_long() {
		return cust_long;
	}
	public void setCust_long(String cust_long) {
		this.cust_long = cust_long;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getPlaceName() {
		return placeName;
	}
	public List<MenuJson> getMenu() {
		return menu;
	}
	public void setMenu(List<MenuJson> menu) {
		this.menu = menu;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	
	public String getPlaceLat() {
		return placeLat;
	}
	public void setPlaceLat(String placeLat) {
		this.placeLat = placeLat;
	}
	public String getPlaceLong() {
		return placeLong;
	}
	public void setPlaceLong(String placeLong) {
		this.placeLong = placeLong;
	}
	public String getPlaceLocation() {
		return placeLocation;
	}
	public void setPlaceLocation(String placeLocation) {
		this.placeLocation = placeLocation;
	}
          
}
