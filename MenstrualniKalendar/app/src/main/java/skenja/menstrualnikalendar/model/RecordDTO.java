package skenja.menstrualnikalendar.model;

import skenja.menstrualnikalendar.Helper;
import skenja.menstrualnikalendar.Repositories.DateDotNet;

public class RecordDTO {

    public int id;

    public DateDotNet date;

    //0 = none, 1 - 3 = some, instensity
    public int Period;

    //0 = none, 1 - 3 = some, instensity
    public int Headache;

    //0 = none, 1 = unprotected, 2 = protected
    public int Coitus;

    public String Comment;

    public boolean Pill;

    public RecordDTO() {
        this.date = new DateDotNet();
    }

    public RecordDTO(Record r)
    {
        this.id = r.id;
        this.date = Helper.Parse(r.date);
        this.Period = r.Period;
        this.Headache = r.Headache;
        this.Coitus = r.Coitus;
        this.Comment = r.Comment;
        this.Pill = r.Pill;
    }
}
