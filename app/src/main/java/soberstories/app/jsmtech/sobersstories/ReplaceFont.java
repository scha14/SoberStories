package soberstories.app.jsmtech.sobersstories;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by Sukriti on 6/7/16.
 */
public class ReplaceFont {

    public static void replaceDefaultFont(Context context, String nameOfFontBeingReplaced, String nameOfFontReplacedWith) throws NoSuchFieldException {

        Typeface customFontTypeface = Typeface.DEFAULT.createFromAsset(context.getAssets(), nameOfFontReplacedWith);
        replaceFont(nameOfFontBeingReplaced, customFontTypeface);
    }

    private static void replaceFont(String nameOfFontBeingReplaced, Typeface customFontTypeface) throws NoSuchFieldException {
        try {
            Field field = Typeface.class.getDeclaredField(nameOfFontBeingReplaced);
            field.setAccessible(true);
            field.set(null, customFontTypeface);

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
