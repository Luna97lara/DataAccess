package dao.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    public LocalDate unmarshal(String v) {
        return (v == null || v.isBlank()) ? null : LocalDate.parse(v);
    }

    public String marshal(LocalDate v)  {
        return (v == null) ? null : v.toString();
    }
}
