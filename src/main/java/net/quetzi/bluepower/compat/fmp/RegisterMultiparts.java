package net.quetzi.bluepower.compat.fmp;

import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;
import net.quetzi.bluepower.api.part.PartRegistry;
import net.quetzi.bluepower.references.Refs;

public class RegisterMultiparts implements IPartFactory
{

    private RegisterMultiparts()
    {

    }

    public static void register()
    {

        String[] parts = PartRegistry.getRegisteredParts().toArray(new String[0]);

        for (int i = 0; i < parts.length; i++)
            parts[i] = Refs.MODID + "_" + parts[i];

        MultiPartRegistry.registerParts(new RegisterMultiparts(), parts);
    }

    @Override
    public TMultiPart createPart(String id, boolean client)
    {

        //BPPart part = PartRegistry.createPart(id, true);
        //if (part != null) return new MultipartBPPart(part);

        return null;
    }

}
