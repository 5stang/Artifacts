package draco18s.artifacts.item;

import java.util.List;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import draco18s.artifacts.api.ArtifactsAPI;
import draco18s.artifacts.api.interfaces.IArtifactComponent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemArtifactArmor extends ItemArmor {
	public static ItemArtifactArmor hcloth;
	public static ItemArtifactArmor hchain;
	public static ItemArtifactArmor hiron;
	public static ItemArtifactArmor hgold;
	public static ItemArtifactArmor hdiamond;
	public static ItemArtifactArmor ccloth;
	public static ItemArtifactArmor cchain;
	public static ItemArtifactArmor ciron;
	public static ItemArtifactArmor cgold;
	public static ItemArtifactArmor cdiamond;
	public static ItemArtifactArmor lcloth;
	public static ItemArtifactArmor lchain;
	public static ItemArtifactArmor liron;
	public static ItemArtifactArmor lgold;
	public static ItemArtifactArmor ldiamond;
	public static ItemArtifactArmor bcloth;
	public static ItemArtifactArmor bchain;
	public static ItemArtifactArmor biron;
	public static ItemArtifactArmor bgold;
	public static ItemArtifactArmor bdiamond;
    
    public static boolean doEnchName = true;
    public static boolean doMatName = true;
    public static boolean doAdjName = true;
    private int iconn;

	public ItemArtifactArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int renderID, int iconNum, int damageIndex) {
		super(par1, par2EnumArmorMaterial, renderID, damageIndex);
		setUnlocalizedName("ArtifactArmor"+par2EnumArmorMaterial.ordinal());
		iconn = iconNum;
		if(iconNum != 2) {
	        this.setCreativeTab(null);
		}
	}
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconReg)
    {
		itemIcon = iconReg.registerIcon("artifacts:artifact"+iconn);
    }
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		//slot will tell us helmet vs. boots
		//type will be either null or overlay (cloth armor)
		//can use stack.stackTagCompound.getString("matName") for material, etc.
		//return "artifacts:textures/armor/Models/artifact_layer1.png";
		return super.getArmorTexture(stack, entity, slot, type);
	}
	
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	public Icon getIcon(ItemStack stack, int pass)
    {
		Icon i = itemIcon;
		if(pass == 0) {
			if(stack.stackTagCompound == null) {
				return itemIcon;
			}
			i = (Icon) ArtifactsAPI.itemicons.icons.get(stack.stackTagCompound.getString("icon").toLowerCase());
			if(i == null) {
				i = itemIcon;
			}
		}
		else {
			if(stack.stackTagCompound == null) {
				return (Icon) ArtifactsAPI.itemicons.icons.get("overlay_artifact1");
			}
			i = (Icon) ArtifactsAPI.itemicons.icons.get("overlay_"+stack.stackTagCompound.getString("icon").toLowerCase());
			if(i == null) {
				i = (Icon) ArtifactsAPI.itemicons.icons.get("overlay_artifact1");
			}
		}
		return i;
    }
	
	@SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int pass)
    {
		if(pass == 0)
			return 16777215;
		else {
			if(par1ItemStack.stackTagCompound != null) {
				return (int) par1ItemStack.stackTagCompound.getLong("overlay_color");	
			}
			return 255;
		}
    }
	
	@Override
	public int getMaxDamage(ItemStack stack)
    {
		float base = 1;
		if(stack.stackTagCompound != null) {
			base = (stack.stackTagCompound.getInteger("material") / 2);
			if(base == 2) {
				base += 3;
			}
			else {
				base += 7.5;
			}		
		}
		return (int) (Math.pow(2, base)-1);
    }

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		NBTTagCompound data = item.getTagCompound();
		int effectID = 0;
		if(data != null) {
			effectID = data.getInteger("onDroppedByPlayer");
			if(effectID != 0) {
				IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
				return c.onDroppedByPlayer(item, player);
			}
			effectID = data.getInteger("onArmorTickUpdate");
			if(effectID != 0) {
				IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
				return c.onDroppedByPlayer(item, player);
			}
		}
		return true;
	}
	
	/*public boolean isValidArmor(ItemStack stack, int armorType, Entity entity)
    {
        return armorType == stack.stackTagCompound.getInteger("armorType");
    }*/
	
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack)
    {
		NBTTagCompound data = itemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			if(!world.isRemote) {
				effectID = data.getInteger("onArmorTickUpdate");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onArmorTickUpdate(world, player, itemStack, true);
				}
			}
		}
		else if(!world.isRemote) {
			itemStack = ArtifactsAPI.artifacts.applyRandomEffects(itemStack);
		}
    }
	
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        return false;
    }
    
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block)
    {
		return 0.25F;
    }
	
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
    }
	
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
		return false;
    }

	@Override
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int blockID, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase)
    {
    	return false;
    }
    
    public boolean canHarvestBlock(Block par1Block, ItemStack itemStack)
    {
    	return false;
    }
    
    public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase)
    {
    	return false;
    }

    @Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		ItemStack stack = entityItem.getEntityItem();
		NBTTagCompound data = stack.stackTagCompound;
		World par2World = entityItem.worldObj; 
		int effectID = 0;
		if(data != null && entityItem.age % 15 == 0) {
			if(!par2World.isRemote) {
				effectID = data.getInteger("onUpdate");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onEntityItemUpdate(entityItem,"onUpdate");
				}
				effectID = data.getInteger("onEntityItemUpdate");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onEntityItemUpdate(entityItem,"onEntityItemUpdate");
				}
				effectID = data.getInteger("onDropped");
				if(effectID != 0) {
					int del = data.getInteger("droppedDelay");
					if(del <= entityItem.age) {
						IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
						c.onEntityItemUpdate(entityItem,"onDropped");
					}
				}
				effectID = data.getInteger("onArmorTickUpdate");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onEntityItemUpdate(entityItem,"onUpdate");
				}
			}
		}
		return false;
	}
    
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    	NBTTagCompound data = par1ItemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			if(!par2World.isRemote) {
				effectID = data.getInteger("onUpdate");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
				}
				effectID = data.getInteger("onArmorTickUpdate");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onArmorTickUpdate(par2World, (EntityPlayer)par3Entity, par1ItemStack, false);
				}
			}
		}
		else if(!par2World.isRemote) {
			par1ItemStack = ArtifactsAPI.artifacts.applyRandomEffects(par1ItemStack);
		}
    }
    
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.none;
    }
    
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
    	
    }
    
    public Multimap getItemAttributeModifiers()
    {
    	/*Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)this.weaponDamage, 0));
        return multimap;*/
    	return super.getItemAttributeModifiers();
    }
    
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
    	NBTTagCompound data = par1ItemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			//par1ItemStack = Artifact.applyRandomEffects(par1ItemStack);
			//data = par1ItemStack.getTagCompound();
		//}
			IArtifactComponent c;
			
			effectID = data.getInteger("onArmorTickUpdate");
			//System.out.println("Effect ID: " + effectID);
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when worn.", advTooltip);
			}
			effectID = data.getInteger("onUpdate");
			//System.out.println("Effect ID: " + effectID);
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "passively.", advTooltip);
			}
		}
    }

    public String getItemDisplayName(ItemStack par1ItemStack)
    {
    	String n = "";
    	if(par1ItemStack.stackTagCompound != null) {
    		if(doEnchName && doMatName && doAdjName) {
	    		n = par1ItemStack.stackTagCompound.getString("name");
    		}
    		else {
    			if(doEnchName) {
    				n += par1ItemStack.stackTagCompound.getString("enchName") + " ";
    			}
    			if(doAdjName) {
    				n += par1ItemStack.stackTagCompound.getString("preadj") + " ";
    			}
    			if(doMatName) {
    				n += par1ItemStack.stackTagCompound.getString("matName") + " ";
    			}
    			if(!(doEnchName || doMatName || doAdjName)) {
    				n += "Artifact ";
    			}
    			n += par1ItemStack.stackTagCompound.getString("iconName");
    			if(doAdjName) {
    				n += " " + par1ItemStack.stackTagCompound.getString("postadj");
    			}
    		}
    	}
		if(n.length() < 1) {
			n = "Artifact";
		}
        return n;//("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(par1ItemStack) + ".name")).trim();
    }
}