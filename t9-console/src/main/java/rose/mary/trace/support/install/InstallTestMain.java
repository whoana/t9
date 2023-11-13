package rose.mary.trace.support.install;

import java.util.Scanner;

public class InstallTestMain {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);

        while(console.hasNextLine()) {
            System.out.print("type input:");
            String  line = console.nextLine();
            System.out.println("your input:[" +line + "]");
            if(line.contains("ok")) break;
        }
    }
}
