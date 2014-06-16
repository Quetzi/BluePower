/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.items;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class ItemCrafting extends net.minecraft.item.Item
{
	public static ArrayList<CraftingIngredient> ingredients = new ArrayList<CraftingIngredient>();

	public enum CraftingIngredient
	{
		STONE_WAFER(Refs.ITEMSTONEWAFER_NAME, Refs.ITEMSTONEWAFER_NAME),
		STONE_WIRE(Refs.ITEMSTONEWIRE_NAME, Refs.ITEMSTONEWIRE_NAME),
		STONE_ANODE(Refs.ITEMSTONEANODE_NAME, Refs.ITEMSTONEANODE_NAME),
		STONE_CATHODE(Refs.ITEMSTONECATHODE_NAME, Refs.ITEMSTONECATHODE_NAME),
		STONE_POINTER(Refs.ITEMSTONEPOINTER_NAME, Refs.ITEMSTONEPOINTER_NAME),
		SILICON_CHIP(Refs.ITEMSILICONCHIP_NAME, Refs.ITEMSILICONCHIP_NAME),
		TAINTEDSILICON_CHIP(Refs.ITEMTAINTEDSILICONCHIP_NAME, Refs.ITEMTAINTEDSILICONCHIP_NAME),
		STONE_REDWIRE(Refs.ITEMSTONEREDWIRE_NAME, Refs.ITEMSTONEREDWIRE_NAME),
		PLATE_ASSEMBLY(Refs.ITEMPLATEASSEMBLY_NAME, Refs.ITEMPLATEASSEMBLY_NAME),
		STONE_BUNDLE(Refs.ITEMSTONEBUNDLE_NAME, Refs.ITEMSTONEBUNDLE_NAME);

		String unlocalizedName;
		String iconString;
		public int damage;
		IIcon icon;

		public ItemStack is;

		CraftingIngredient(String unlocalizedName, String iconString)
		{
			this.unlocalizedName = unlocalizedName;
			this.iconString = iconString;
			this.damage = ingredients.size();
			this.is = new ItemStack(BPItems.craftingItems, 1, damage);

			ingredients.add(this);
		}

		public ItemStack is()
		{
			return is;
		}
	}

	public ItemCrafting(String name)
	{
		this.setCreativeTab(CustomTabs.tabBluePowerItems);
		this.setUnlocalizedName(name);
		this.setHasSubtypes(true);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister ir)
	{
		for (CraftingIngredient ci : ingredients)
		{
			ci.icon = ir.registerIcon(Refs.MODID + ":" + ci.iconString);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int damage)
	{
		return ingredients.get(damage).icon;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
	{
		for (int i = 0; i < ingredients.size(); ++i)
		{
			p_150895_3_.add(new ItemStack(p_150895_1_, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		return "item." + ingredients.get(is.getItemDamage()).unlocalizedName;
	}
}
