package org.justserve.util;

import io.micronaut.context.annotation.Value;
import org.justserve.command.BaseCommand;
import picocli.CommandLine;


public class JustServeVersionProvider extends BaseCommand implements CommandLine.IVersionProvider  {

    @Value("${micronaut.application.version}")
    String justserveCliVersion;

    String fancyPrintout = """
                 _           _    ____
                | |         | |  / ___|
                | |_   _ ___| |_| (___   ___ _ ____   _____
                | | | | / __| __|\\___ \\ / _ \\ '__\\ \\ / / _ \\
             ___| | |_| \\__ \\ |_ ____) |  __/ |   \\ V /  __/
            |____/ \\__,_|___/\\__|_____/ \\___|_|    \\_/ \\___|""";

    @Override
    public String[] getVersion() {
        return new String[]{applyStyle(fancyPrintout, orange), applyStyle(justserveCliVersion, blue)};
    }
}
