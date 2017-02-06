package binnie.extratrees.kitchen.craftgui;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.RenderUtil;
import binnie.craftgui.minecraft.Window;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;

import javax.annotation.Nullable;

public class ControlFluidDisplay extends Control implements ITooltip {
	@Nullable
	private FluidStack fluidStack;
	private boolean hastooltip;

	public void setTooltip() {
		this.hastooltip = true;
		this.addAttribute(Attribute.MouseOver);
	}

	public ControlFluidDisplay(final IWidget parent, final int x, final int y) {
		this(parent, x, y, 16);
	}

	public ControlFluidDisplay(final IWidget parent, final int x, final int y, final FluidStack stack, final boolean tooltip) {
		this(parent, x, y, 16);
		this.setFluidStack(stack);
		if (tooltip) {
			this.setTooltip();
		}
	}

	public ControlFluidDisplay(final IWidget parent, final int x, final int y, final int size) {
		super(parent, x, y, size, size);
		this.fluidStack = null;
		this.hastooltip = false;
	}

	@Override
	public void onRenderForeground(int guiWidth, int guiHeight) {
		if (this.fluidStack == null) {
			return;
		}
		final IPoint relativeToWindow = this.getAbsolutePosition().sub(this.getSuperParent().getPosition());
		if (relativeToWindow.x() > Window.get(this).getSize().x() + 100 || relativeToWindow.y() > Window.get(this).getSize().y() + 100) {
			return;
		}
		if (this.fluidStack != null) {
			final Fluid fluid = this.fluidStack.getFluid();
			final int hex = fluid.getColor(this.fluidStack);
			final int r = (hex & 0xFF0000) >> 16;
			final int g = (hex & 0xFF00) >> 8;
			final int b = hex & 0xFF;
			final ResourceLocation iconRL = this.fluidStack.getFluid().getStill();
			TextureAtlasSprite icon = FMLClientHandler.instance().getClient().getTextureMapBlocks().getAtlasSprite(iconRL.toString());
			GlStateManager.color(r / 255.0f, g / 255.0f, b / 255.0f, 1.0f);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
			if (this.getSize().x() != 16) {
				GlStateManager.pushMatrix();
				final float scale = this.getSize().x() / 16.0f;
				GlStateManager.scale(scale, scale, 1.0f);
				RenderUtil.drawSprite(IPoint.ZERO, icon);
				GlStateManager.popMatrix();
			} else {
				RenderUtil.drawSprite(IPoint.ZERO, icon);
			}
			GlStateManager.disableBlend();
		}
	}

	public void setFluidStack(final FluidStack fluidStack) {
		this.fluidStack = fluidStack;
	}

	@Override
	public void getTooltip(final Tooltip tooltip) {
		if (this.hastooltip && this.fluidStack != null) {
			tooltip.add(this.fluidStack.getLocalizedName());
		}
	}
}