package elec332.cmip.util;

import elec332.core.config.Configurable;

/**
 * Created by Elec332 on 19-10-2015.
 */
@Configurable.Class(category = "handlers", comment = "In this category you can find a broken-down list of all the handlers in this mod, with the option to enable/disable them.")
public class Config {

    @Configurable.Class
    public static class NEI {

        @Configurable.Class
        public static class AE2 {

            @Configurable(comment = "If set to true, will hide all but one of all types of AE2 facades.")
            public static boolean hideAE2Facades = true;

        }

        @Configurable.Class
        public static class BiblioCraft {

            @Configurable(comment = "If set to true, will hide all but one from all the EG bookstands from NEI.")
            public static boolean hideDuplicateBlocks = true;

        }

        @Configurable.Class
        public static class BuildCraft {

            @Configurable(comment = "If set to true, will hide all but one of all types of BuildCraft facades.")
            public static boolean hideBuildCraftFacades = true;

        }

        @Configurable.Class
        public static class ExtraUtilities {

            @Configurable(comment = "If set to true, will hide all but one of all types of ExU MultiParts.")
            public static boolean hideExtraUtilitiesMultiParts = true;


            @Configurable
            public static boolean hideFilledDrums = true;

        }

        @Configurable.Class
        public static class FMP {

            @Configurable(comment = "If set to true, will hide all but one of all types of FMP MultiParts.")
            public static boolean hideFMPMultiParts = true;

            @Configurable(comment = "Set to true if you want to see a recipes for all MultiParts in NEI, WIP")
            public static boolean addMultiPartRecipes = false;

        }

        @Configurable.Class
        public static class IC2 {

            @Configurable(comment = "If true, will hide all blocks that can't/shouldn't be placed directly from NEI.")
            public static boolean hideUnplacableBlocks = true;

            @Configurable
            public static boolean addUUMatterRecipes = true;

        }

        @Configurable.Class
        public static class RFTools {

            @Configurable(comment = "If true, will add '?' support to the RFTools Crafter (all tiers).")
            public static boolean addNEICrafterSupport = true;

        }

    }

    @Configurable.Class
    public static class WAILA {

        @Configurable.Class
        public static class AE2 {

            @Configurable(comment = "Adds information (Like state, frequency) to the WAILA hud.")
            public static boolean p2p = true;

            @Configurable(comment = "Adds the frequency of a quantum bridge to the WAILA hud")
            public static boolean qb = true;

        }

        @Configurable.Class
        public static class BuildCraft {

            @Configurable
            public static boolean energy = true;

            @Configurable
            public static boolean avgLaserEnergy = true;

            @Configurable
            public static boolean heat = true;

            @Configurable
            public static boolean robot = true;

        }

        @Configurable.Class
        public static class ChickenChunks{

            @Configurable(comment = "If set to true, only the owner and operators will be able to see the data of the chunkloader")
            public static boolean showOnlyIfAccess = true;

        }

        @Configurable.Class
        public static class DraconicEvolution{

            @Configurable
            public static boolean showLinkedDevices = true;

        }

        @Configurable.Class
        public static class ExtraUtilities{

            @Configurable
            public static boolean showPipeData = true;

        }

        @Configurable.Class
        public static class Forestry{

            @Configurable
            public static boolean showAccessData = true;

            @Configurable
            public static boolean showBeeData = true;

            @Configurable(comment = "If true, shows information telling you whether you have new mail or not. (When hovering over a mailbox)")
            public static boolean showMailData = true;

        }

        @Configurable.Class(category = "Forge/Vanilla")
        public static class Forge{

            @Configurable(comment = "Set to true to show fluid data for all tanks, might cause duplicate data.")
            public static boolean showTankInfo = false;

            @Configurable
            public static boolean showVillagerProfession = true;

        }

        @Configurable.Class
        public static class IC2{

            @Configurable
            public static boolean showProgress = true;

            @Configurable(comment = "If true, shows the coords of the position the teleporter is linked to.")
            public static boolean tpLocation = true;

        }

        @Configurable.Class
        public static class MagnetiCraft{

            @Configurable(comment = "MagnetiCraft seems to handle this by itself most of the times")
            public static boolean showHeat = false;

        }

        @Configurable.Class
        public static class MystCraft{

            @Configurable(comment = "If true, shows data about the dimension you will be teleported to (Dimension ID, Dimension name), for the bookstand, lectern, and book receptacle.")
            public static boolean showDimData = true;

        }

    }

}
