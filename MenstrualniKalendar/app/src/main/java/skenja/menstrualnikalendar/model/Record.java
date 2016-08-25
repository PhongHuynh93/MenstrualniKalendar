package skenja.menstrualnikalendar.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

import skenja.menstrualnikalendar.Helper;


@Table(database = Db.class)
public class Record extends BaseModel{

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public Date date;

    //0 = none, 1 - 3 = some, instensity
    @Column
    public int Period;

    //0 = none, 1 - 3 = some, instensity
    @Column
    public int Headache;

    //0 = none, 1 = unprotected, 2 = protected
    @Column
    public int Coitus;

    @Column
    public String Comment;

    @Column
    public boolean Pill;

    public Record() { }

    public Record(RecordDTO r)
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
