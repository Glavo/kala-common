package kala.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.Module;

public class KalaBaseModule extends Module {
    @Override
    public String getModuleName() {
        return "KalaBaseModule";
    }

    @Override
    public Version version() {
        return VersionUtil.parseVersion(Metadata.KALA_VERSION, "org.glavo.kala", "kala-base");
    }

    @Override
    public void setupModule(SetupContext context) {
        // TODO
    }
}
