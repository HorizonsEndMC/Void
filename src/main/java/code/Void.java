package code;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Void implements ModInitializer {
    public static final String MODID = "void";
    private static Logger LOGGER;

    public static Logger getLogger() {
        if (LOGGER == null) {
            LOGGER = LogManager.getLogger("void");
        }
        return LOGGER;
    }

    public static Identifier id(String path) {
        return new Identifier("void", path);
    }

    @Override
    public void onInitialize() {
    }
}