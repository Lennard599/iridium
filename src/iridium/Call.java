package iridium;

import java.util.HashMap;
import java.util.Set;

public class Call {
    private static HashMap<String, Runnable> Commands = new HashMap<>();

    public void setCommand(String Command ,Runnable method)
    {
        Commands.put(Command, method);
    }

    public void runCommand(String[] input) {
        if (Commands.containsKey(input[0]))
            Commands.get(input[0]).run();
        else
            Presentation.update("command not found!! try lower case or use commands or help",false);
    }

    public boolean checkCommands(String Command)
    {
            return Commands.containsKey(Command);
    }

    public String getCommands(){
        Set<String> a = Commands.keySet();
        String b = "";

        for (String c : a)
            b += c + " | ";
        return b;
    }
}
