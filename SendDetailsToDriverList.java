package Test;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="JsonObject")
@XmlAccessorType(XmlAccessType.FIELD)
public class SendDetailsToDriverList {
	
	@XmlElement
	List<sendDetailsToDriver> details;

	public List<sendDetailsToDriver> getDetails() {
		return details;
	}

	public void setDetails(List<sendDetailsToDriver> details) {
		this.details = details;
	}
	
	

}
