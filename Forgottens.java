package Test;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="JsonObject")
@XmlAccessorType(XmlAccessType.FIELD)
public class Forgottens {

	@XmlElement
	List<Forgotten> product;

	public List<Forgotten> getProduct() {
		return product;
	}

	public void setProduct(List<Forgotten> product) {
		this.product = product;
	}


}
