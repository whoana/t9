package rose.mary.trace.support.install;

import java.util.Locale;
import java.util.ResourceBundle;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import pep.per.mint.common.util.Util;

/**
 * 참고 사이트 : https://docs.oracle.com/javase/tutorial/i18n/intro/steps.html
 * 
 */
public class InstallMessageBundle {
    public static void main(String[] args) {


        String toDate = Util.getFormatedDate("yyyyMMddHH");
        String fromDate = Util.getDateAdd("yyyyMMddHH", toDate, -7);
        System.out.println(toDate+ ":"+ fromDate);
        //Locale localeKoKr = new Locale(Locale.KOREAN, Locale.KOREA);

        ReloadableResourceBundleMessageSource rrbms = new ReloadableResourceBundleMessageSource();
        rrbms.setDefaultEncoding("utf8");
        rrbms.addBasenames("message/InstallMessages");

        String params[] = {"T9"};
        String startInstall = rrbms.getMessage("out.msg.start.install", params, Locale.KOREA);
        //ResourceBundle messages = ResourceBundle.getBundle("message/InstallMessages", Locale.KOREA);
        //String inputYourJdbcUrl = messages.getString("url");
        System.out.println(startInstall);

    }
}
