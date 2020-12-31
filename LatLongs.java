package Test;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="JsonObject")
@XmlAccessorType(XmlAccessType.FIELD)
public class LatLongs {

		@XmlElement
		List<LatLong> latlng;

		public List<LatLong> getLatlng() {
			return latlng;
		}

		public void setLatlng(List<LatLong> latlng) {
			this.latlng = latlng;
		}

		
}
