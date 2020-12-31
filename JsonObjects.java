package Test;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="JsonObject")
@XmlAccessorType(XmlAccessType.FIELD)
public class JsonObjects {

	@XmlElement
	List<JsonObject> cart;
	

	public List<JsonObject> getCart() {
		return cart;
	}

	public void setCart(List<JsonObject> cart) {
		this.cart = cart;
	}
	 
}
