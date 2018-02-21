package iridium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Call {
    private static HashMap<String, Command> Commands = new HashMap<String, Command>();

    public void setCommand(String Command ,Command command)
    {
        Commands.put(Command, command);
    }

    public void runCommand(String[] input) {
        if (Commands.containsKey(input[0].toLowerCase())) {
            if (Commands.get(input[0].toLowerCase()).getMaxparameter() >= input.length-1 && Commands.get(input[0].toLowerCase()).getMinparameter() <= input.length-1)
            Commands.get(input[0].toLowerCase()).getFunction().run();
            else
                Presentation.update("too much or little parameters", false);
        }
        else if (getAliasList().contains(input[0].toLowerCase())) {
            for (String k : Commands.keySet())
                if (Commands.get(k).getAlias().equals(input[0]))
                    if (Commands.get(k).getMaxparameter() >= input.length-1 && Commands.get(k).getMinparameter() <= input.length-1)
                        Commands.get(k).getFunction().run();
                    else
                        Presentation.update("too much or little parameter", false);
        }
        else {
            String a = suggestion(input[0]);
            if (a.isEmpty())
                Presentation.update("command not found!! try to use commands or help", false);
            else
                Presentation.update("command not found!! did you mean " + suggestion(input[0]) + " if not try to use commands or help", false);
        }
    }

    public static boolean checkCommands(String Command ,boolean contains) {
        if (Commands.containsKey(Command)) {
            return true;
        }

        for (String a:Commands.keySet()) {
            if (Commands.get(a).getAlias().equals(Command)) {
                return true;
            }
        }

        if (contains) {
            for (String a : Commands.keySet()) {
                if (Command.startsWith(Commands.get(a).getAlias())) {
                    return true;
                }
            }

            for (String a : Commands.keySet()) {
                if (Command.startsWith(a)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String getCommands(){
        Set<String> a = Commands.keySet();
        String b = "";

        for (String c : a)
            b += c + " | ";
        return b;
    }

    public static ArrayList<String> getCommandsarray(){
        Set<String> a = Commands.keySet();
        ArrayList<String> b = new ArrayList<>();
        for (String c : a)
            b.add(c);
        return b;
    }

    public static String suggestion(String a){
        Set<String> aa = Commands.keySet();
        char[] chars = a.toCharArray();
        int match = 0;
        int bestMatch = 0;
        int pmatch = 0;
        String tmp = "";

        Iterator<String> it = aa.iterator();
        while (it.hasNext()) {
            String command = it.next();
            for (int i = 0; i < chars.length; i++)
                if (command.indexOf(chars[i]) >= 0)
                    match++;
            if (match > 0)
                pmatch = (match/a.length()*100);

            if (pmatch > bestMatch) {
                tmp = command;
                bestMatch = pmatch;
                match = 0;
            }
        }
        //if (pmatch > 50)
         //   return tmp;
        return "";
    }

    public static ArrayList<String> complete(String a){
        if (!a.trim().isEmpty()) {
            ArrayList<String> b = getAliasList();
            ArrayList<String> c = getCommandsarray();
            ArrayList<String> d = new ArrayList<>();

            for (String aa : b)
                if (aa.startsWith(a)) {
                    d.add(b.get(b.indexOf(aa)));
                }
            for (String aa : c)
                if (aa.startsWith(a))
                    d.add(c.get(c.indexOf(aa)));
            return d;
        }
        return new ArrayList<>();
    }

    private static ArrayList<String> getAliasList(){
        ArrayList<String> a = new ArrayList<String>();
        for (String c: Commands.keySet())
            a.add(Commands.get(c).getAlias());

        return a;
    }
}
