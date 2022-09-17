package kala.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

public class KalaBaseModule extends Module {
    @Override
    public String getModuleName() {
        return "KalaBaseModule";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext context) {
        // TODO
    }
}
