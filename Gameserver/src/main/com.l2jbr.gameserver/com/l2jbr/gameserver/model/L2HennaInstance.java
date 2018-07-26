/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jbr.gameserver.model;

import com.l2jbr.gameserver.model.entity.database.Henna;

public class L2HennaInstance {

	private final Henna template;
	
	public L2HennaInstance(Henna template) {
		this.template = template;
	}
	
	public String getName()
	{
		String res = "";
		if (getStatINT() > 0)
		{
			res = res + "INT +" + getStatINT();
		}
		else if (getStatSTR() > 0)
		{
			res = res + "STR +" + getStatSTR();
		}
		else if (getStatCON() > 0)
		{
			res = res + "CON +" + getStatCON();
		}
		else if (getStatMEM() > 0)
		{
			res = res + "MEN +" + getStatMEM();
		}
		else if (getStatDEX() > 0)
		{
			res = res + "DEX +" + getStatDEX();
		}
		else if (getStatWIT() > 0)
		{
			res = res + "WIT +" + getStatWIT();
		}
		
		if (getStatINT() < 0)
		{
			res = res + ", INT " + getStatINT();
		}
		else if (getStatSTR() < 0)
		{
			res = res + ", STR " + getStatSTR();
		}
		else if (getStatCON() < 0)
		{
			res = res + ", CON " + getStatCON();
		}
		else if (getStatMEM() < 0)
		{
			res = res + ", MEN " + getStatMEM();
		}
		else if (getStatDEX() < 0)
		{
			res = res + ", DEX " + getStatDEX();
		}
		else if (getStatWIT() < 0)
		{
			res = res + ", WIT " + getStatWIT();
		}
		
		return res;
	}

	public int getSymbolId() {
		return template.getSymbolId();
	}

	public int getItemIdDye()
	{
		return template.getDyeId();
	}

	
	public int getAmountDyeRequire()
	{
		return template.getDyeAmount();
	}

	
	public int getPrice()
	{
		return template.getPrice();
	}
	
	public int getStatINT()
	{
		return template.getStatINT();
	}
	
	public int getStatSTR()
	{
		return template.getStatSTR();
	}
	
	public int getStatCON()
	{
		return template.getStatCON();
	}

	public int getStatMEM()
	{
		return template.getStatMEM();
	}
	
	public int getStatDEX()
	{
		return template.getStatDEX();
	}
	
	public int getStatWIT()
	{
		return template.getStatWIT();
	}
}
