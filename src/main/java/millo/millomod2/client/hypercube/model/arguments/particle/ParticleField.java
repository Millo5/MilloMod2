package millo.millomod2.client.hypercube.model.arguments.particle;

import com.google.gson.JsonObject;
import millo.millomod2.client.util.MilloLog;

import java.util.HashMap;
import java.util.function.Supplier;

public abstract class ParticleField<T extends ParticleField<T>> {

    abstract T self();
    public abstract void deserialize(JsonObject jsonObject);
    public abstract void serializeOn(JsonObject jsonObject);

    public abstract String id();


    ///


    private static final HashMap<String, Supplier<ParticleField<?>>> arguments = new HashMap<>();
    static {
        register(ColorParticleField::new);
        register(ColorVariationParticleField::new);
        register(DurationParticleField::new);
        register(FadeColorParticleField::new);
        register(MaterialParticleField::new);
        register(MotionParticleField::new);
        register(MotionVariationParticleField::new);
        register(RollParticleField::new);
        register(SizeParticleField::new);
        register(SizeVariationParticleField::new);
    }

    private static void register(Supplier<ParticleField<?>> supp) {
        ParticleField<?> arg = supp.get();
        arguments.put(arg.id(), supp);
    }

    public static ParticleField<?> getParticleField(String fieldName) {
        Supplier<ParticleField<?>> supp = arguments.get(fieldName);
        if (supp == null) throw MilloLog.throwError("Unknown particle field: " + fieldName);
        return supp.get();
    }


}
