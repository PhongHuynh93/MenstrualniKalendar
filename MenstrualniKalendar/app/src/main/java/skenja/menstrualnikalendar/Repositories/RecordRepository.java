package skenja.menstrualnikalendar.Repositories;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import skenja.menstrualnikalendar.Helper;
import skenja.menstrualnikalendar.model.Record;
import skenja.menstrualnikalendar.model.RecordDTO;
import skenja.menstrualnikalendar.model.Record_Table;

public class RecordRepository {

    public static void Insert(Record r)
    {
        r.save();
    }

    public static RecordDTO GetModel(DateDotNet d)
    {
        Record r = RecordRepository.GetModelFromDb(d);
        if(r != null)
            return new RecordDTO(r);
        else
            return null;
    }

    private static Record GetModelFromDb(DateDotNet d)
    {
        Date date = Helper.Parse(new DateDotNet(d.Day, d.Month, d.Year));

        return new Select()
                .from(Record.class)
                .where(Record_Table.date.eq(date))
                .querySingle();
    }

    public static List<RecordDTO> GetAll()
    {
        List<Record> list = new Select().from(Record.class).queryList();
        List<RecordDTO> listDTO = new ArrayList<RecordDTO>();

        for (Record r : list) {
            listDTO.add(new RecordDTO(r));
        }

        return listDTO;
    }

    public static List<RecordDTO> GetList(DateDotNet dStart, DateDotNet dEnd)
    {
        List<Record> list = RecordRepository.GetModelList(dStart, dEnd);
        List<RecordDTO> listDTO = new ArrayList<RecordDTO>();

        for (Record r : list) {
            listDTO.add(new RecordDTO(r));
        }

        return listDTO;
    }

    private static List<Record> GetModelList(DateDotNet dStart, DateDotNet dEnd)
    {
        Date dateStart = Helper.Parse(dStart);
        Date dateEnd = Helper.Parse(dEnd);

        return new Select()
                .from(Record.class)
                .where(Record_Table.date.between(dateStart).and(dateEnd))
                .queryList();
    }

    public static void Insert(RecordDTO r)
    {
        Record record = new Record(r);
        record.save();
    }

    public static void Update(RecordDTO r)
    {
        Record record = new Select().from(Record.class).where(Record_Table.id.eq(r.id)).querySingle();

        if(record == null)
            return;

        if(record.Period != r.Period)
            record.Period = r.Period;
        if(record.Coitus != r.Coitus)
            record.Coitus = r.Coitus;
        if(record.Headache != r.Headache)
            record.Headache = r.Headache;

        record.Comment = r.Comment;
        record.Pill = r.Pill;

        record.save();
    }

    public static void Delete(RecordDTO r)
    {
        Record record = new Select().from(Record.class).where(Record_Table.id.eq(r.id)).querySingle();

        if(record == null)
            return;

        record.delete();
    }

    public static void DeleteAll()
    {
        new Thread(new Runnable(){
                    @Override
                    public void run() {
                        new Delete().from(Record.class).query();
                    }
                }
        ).run();
    }
}
