package kala.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.Module;

public class KalaCollectionModule extends Module {
    @Override
    public String getModuleName() {
        return "KalaCollectionModule";
    }

    @Override
    public Version version() {
        return VersionUtil.parseVersion(Metadata.KALA_VERSION, "org.glavo.kala", "kala-collection");
    }

    @Override
    public void setupModule(SetupContext context) {
        //context.addDeserializers(new KalaBaseDeserializers());
        context.addSerializers(new KalaCollectionSerializers());
        context.addTypeModifier(new KalaCollectionTypeModifier());
    }
}