package minicraft.entity.mob;

import minicraft.core.Updater;
import minicraft.core.io.Settings;
import minicraft.gfx.MobSprite;
import minicraft.gfx.Screen;
import minicraft.item.Items;

public class Sheep extends PassiveMob {
	private static final MobSprite[][] sprites = MobSprite.compileMobSpriteAnimations(0, 28);
	private static final MobSprite[][] cutSprites = MobSprite.compileMobSpriteAnimations(0, 24);

	private static final int WOOL_GROW_TIME = 3 * 60 * Updater.normSpeed; // Three minutes

	public boolean cut = false;
	private int ageWhenCut = 0;

	/**
	 * Creates a sheep entity.
	 */
	public Sheep() {
		super(sprites);
	}

	@Override
	public void render(Screen screen) {
		int xo = x - 8;
		int yo = y - 11;

		MobSprite[][] curAnim = cut ? cutSprites : sprites;

		MobSprite curSprite = curAnim[dir.getDir()][(walkDist >> 3) % curAnim[dir.getDir()].length];
		if (hurtTime > 0) {
			curSprite.render(screen, xo, yo, true);
		} else {
			curSprite.render(screen, xo, yo);
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (age - ageWhenCut > WOOL_GROW_TIME) cut = false;
	}

	/** This function is called when shear item interacts on this sheep. */
	public void shear() {
		cut = true;
		dropItem(1, 3, Items.get("Wool"));
		ageWhenCut = age;
	}

	public void die() {
		int min = 0, max = 0;
		if (Settings.get("diff").equals("Easy")) {min = 1; max = 3;}
		if (Settings.get("diff").equals("Normal")) {min = 1; max = 2;}
		if (Settings.get("diff").equals("Hard")) {min = 0; max = 2;}

		if (!cut) dropItem(min, max, Items.get("wool"));
		dropItem(min, max, Items.get("Raw Beef"));

		super.die();
	}
}