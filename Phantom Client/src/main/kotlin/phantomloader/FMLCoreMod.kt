package phantomloader

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin

/**
 * Entry of the loader
 */
class FMLCoreMod : IFMLLoadingPlugin {

    init {
        launch()
    }

    override fun getModContainerClass(): String? = null

    override fun getASMTransformerClass(): Array<String> = emptyArray()

    override fun getSetupClass(): String? = null

    override fun injectData(data: Map<String?, Any?>) {
    }

    override fun getAccessTransformerClass(): String? {
        return null
    }

}