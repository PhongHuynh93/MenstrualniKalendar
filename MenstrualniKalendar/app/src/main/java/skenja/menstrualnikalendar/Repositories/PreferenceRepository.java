package skenja.menstrualnikalendar.Repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class PreferenceRepository {

    private PreferenceRepository() { }

    public static boolean DBExists(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("DB", false);
    }

    public static boolean DBDoesExist(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("DB", !DBExists(context)).commit();
    }

    public static boolean ValidateControl(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("control_key", false);
    }

    public static boolean ValidatePIN(String input, Context context)
    {
        return input.equals(PreferenceManager.getDefaultSharedPreferences(context).getString("PIN", "ovo nece nikada proci dok bi prazan string mogao u slucaju da ne dohvati postavke"));
    }

    public static boolean ChangePIN(String oldPin, String newPin, Context context)
    {
        if(ValidatePIN(oldPin, context))
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString("PIN", newPin)
                    .commit();
        else
            return false;
    }

    public static boolean PillStatus(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("Pill", false);
    }

    public static boolean ChangePillStatus(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean("Pill", !PillStatus(context)) //puts oposite of current state
            .commit();
    }

    public static boolean LayoutIsGrid(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("isGridLayout", true);
    }

    public static boolean ChangeLayout(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean("isGridLayout", !LayoutIsGrid(context)) //puts oposite of current state
                .commit();
    }
}
