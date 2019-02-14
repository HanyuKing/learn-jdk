package other;

import sun.jvm.hotspot.debugger.AddressException;
import sun.jvm.hotspot.memory.StringTable;
import sun.jvm.hotspot.oops.*;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.tools.Tool;
import sun.jvm.hotspot.utilities.BasicHashtable;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

public class StringInternDump extends Tool {
    public void run() {
        // Ready to go with the database...
        try {
            final ArrayList<Instance> stringIns = new ArrayList<Instance>(128);

            StringTable stringTable = VM.getVM().getStringTable();

            final PrintStream ps = new PrintStream("StringInternDump.txt");

            stringTable.stringsDo(new StringTable.StringVisitor() {
                public void visit(Instance instance) {
                    stringIns.add(instance);
                    //instance.printValueOn(ps);
                    //ps.println();
                    instance.printValue();
                    System.out.println();
                }
            });

            System.out.println("string table count " + stringIns.size());

        } catch (AddressException e) {
            System.err.println("Error accessing address 0x"
                               + Long.toHexString(e.getAddress()));
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        args = new String[]{"38528"};
        StringInternDump tool = new StringInternDump();
        tool.execute(args);
        tool.stop();
    }
}