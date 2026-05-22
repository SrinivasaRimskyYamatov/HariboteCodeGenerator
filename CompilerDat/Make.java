import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Make {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Make program.c");
            return;
        }

        String source = args[0];

        if (!source.endsWith(".c")) {
            System.out.println("Error: source must be a .c file");
            return;
        }

        String name = source.substring(0, source.length() - 2);

        String makefile =
                "CC = gcc\n" +
                "CFLAGS = -nostdlib -m32 -fno-builtin -fno-pie -fno-pic -O0 -fno-common\n" +
                "\n" +
                "LFLAGS = -m elf_i386\n" +
                "\n" +
                "TARGET_DIR = ../bin/app\n" +
                "\n" +
                "LIB = $(TARGET_DIR)/lib/apilib.lib\n" +
                "LS = ../scripts/app_large_stack.lds\n" +
                "\n" +
                "STDLIB_DIR = ../tools/stdlibc\n" +
                "STDLIB = $(STDLIB_DIR)/bin/stdlibc.o\n" +
                "\n" +
                "DIST_DIR = $(TARGET_DIR)/dist\n" +
                "TMP_DIR = $(TARGET_DIR)/tmp/" + name + "\n" +
                "\n" +
                "APP = $(DIST_DIR)/" + name + ".hrb\n" +
                "APP_SRC = " + source + "\n" +
                "\n" +
                "INCLUDE = -I ../lib/include -I $(STDLIB_DIR)/include\n" +
                "\n" +
                "$(APP): $(APP_SRC) $(LIB)\n" +
                "\tmkdir -p $(DIST_DIR)\n" +
                "\tmkdir -p $(TMP_DIR)\n" +
                "\t$(CC) -c $(CFLAGS) $(INCLUDE) -o $(TMP_DIR)/$(APP_SRC:.c=.o) $(APP_SRC)\n" +
                "\n" +
                "\tld $(LFLAGS) -o $(APP) -e HariMain -T $(LS) \\\n" +
                "\t\t$(STDLIB) \\\n" +
                "\t\t$(TMP_DIR)/$(APP_SRC:.c=.o) \\\n" +
                "\t\t$(LIB)\n" +
                "\n" +
                "$(LIB):\n" +
                "\tcd ../lib; make\n" +
                "\n" +
                "$(STDLIB):\n" +
                "\tcd $(STDLIB_DIR); make\n" +
                "\n" +
                "clean:\n" +
                "\trm -rf $(TMP_DIR)\n" +
                "\trm -f $(APP)\n";

        try {
            Path wplace = Paths.get("wplace");

            if (!Files.exists(wplace)) {
                System.out.println("Error: wplace directory not found");
                return;
            }

            Path sourceFile = wplace.resolve(source);

            if (!Files.exists(sourceFile)) {
                System.out.println("Error: source file not found: " + sourceFile);
                return;
            }

            Path makefilePath = wplace.resolve("Makefile");
            Files.writeString(makefilePath, makefile);

            System.out.println("Generated: " + makefilePath);

            ProcessBuilder pb = new ProcessBuilder("make");
            pb.directory(wplace.toFile());
            pb.inheritIO();

            Process process = pb.start();
            int exitCode = process.waitFor();

            System.out.println("make exited with code " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}