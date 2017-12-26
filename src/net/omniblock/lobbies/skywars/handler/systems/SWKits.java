package net.omniblock.lobbies.skywars.handler.systems;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import net.omniblock.skywars.util.ItemBuilder;

import java.util.Arrays;
import java.util.List;

public class SWKits {

	public enum KitKind {

		FREE("&6&lKits Gratuitos"), 
		GENERAL("&2&lKits Generales");

		private String inventoryName;

		KitKind(String inventoryName) {

			this.inventoryName = inventoryName;
			

		}

		public String getInventoryName() {
			return inventoryName;
		}
	}

	public enum SWKitsType {

		CABALLERO(KitKind.GENERAL, "Caballero", "&e✯✯✯✯", "K0", Material.IRON_SWORD,
				new KitContents(KitContents.KIT_CABALLERO_CONTENTS), 4, 354, 
				new String[] { 
						"&8- &7Un blindage ligero,",
						"&7hecho para una mayor!",
						"&7agilidad en combate."}),

		ATLETA(KitKind.GENERAL, "Atleta" ,"&e✯✯", "K1", Material.LEATHER_BOOTS,
				new KitContents(KitContents.KIT_ATLETA_CONTENTS), 2, 183, 
				new String[] {
						"&8- &7El trabajo duro te ofrece",
						"&7con una agilidad única!",
						"&7una mayor velocidad",
						"&7y resistencia física."}),

		ARQUERO(KitKind.GENERAL, "Arquero", "&e✯✯✯✯", "K2", Material.BOW,
				new KitContents(KitContents.KIT_ARQUERO_CONTENTS), 4, 344, 
				new String[] {
						"&8- &7Precisión en sus tiros, capaz de",
						"&7matar a los enemigos más lejanos!"}),

		MAESTRE(KitKind.GENERAL, "Maestre", " &e✯✯✯✯", "K3", Material.POTION,
				new KitContents(KitContents.KIT_MAESTRE_CONTENTS), 4, 305, 
				new String[] {
						"&8- &7Sus conocmientos sobre",
						"&7la alquimia, adquiridos",
						"&7de una manera poco ortodoxa,",
						"&7el conocimiento es poder."}),

		MORBIDO(KitKind.GENERAL, "Mórbido", "&e✯✯", "K4", Material.COOKED_BEEF,
				new KitContents(KitContents.KIT_MORBIDO_CONTENTS), 2, 183, 
				new String[] {
						"&8- &7No subestimes su tamaño,",
						"&7¡puede ser muy rápido y",
						"&7pillarte desprevenido!"}),

		TALADOR_REAL(KitKind.GENERAL, "Talador real ", "&e✯✯✯✯✯", "K5", Material.STONE_AXE,
				new KitContents(KitContents.KIT_TALADOR_REAL_CONTENTS), 5, 366, 
				new String[] {
						"&8- &7Su larga carrera con el",
						"&7hacha no solo le ha dejado",
						"&7horribles cayos, también le ha",
						"&7otorgado fuerza y destreza."}),

		INFANTE(KitKind.GENERAL, "Infante", "&e✯✯✯✯✯✯", "K6", Material.GOLD_SWORD,
				new KitContents(KitContents.KIT_INFANTE_CONTENTS), 6, 549, 
				new String[] {
						"&8- &7Su apellido le doy su",
						"&7armadura, pero su habilidad",
						"&7se la cultivo el mismo."}),

		SANADOR(KitKind.GENERAL, "Sanador", "&e✯✯✯✯✯✯✯", "K7", Material.POTION, 16417,
				new KitContents(KitContents.KIT_SANADOR_CONTENTS), 7, 772, 
				new String[] {
						"&8- &7Estréchamente conectados",
						"&7con los maestres, los sanadores",
						"&7siempre serán valiosos aliados."}),

		ESPIA(KitKind.GENERAL, "Espia", "&e✯✯✯✯✯✯", "K8", Material.POTION, 16462, 
				new KitContents(KitContents.KIT_ESPIA_CONTENTS), 6, 460, 
				new String[] {
						"&8- &7los espías son asesinos",
						"&7capaces de hacer frente",
						"&7a cualquier situación."}),

		PIROMANO(KitKind.GENERAL, "Pirómano", "&e✯✯✯✯✯✯✯✯", "K9", Material.FLINT_AND_STEEL,
				new KitContents(KitContents.KIT_PIROMANO_CONTENTS), 8, 1091, 
				new String[] {
						"&8- &7Los gritos de sus vícimas",
						"&7alimentan sus llamas,",
						"&7jugando con fuego",
						"&7hasta hacerlo una forma",
						"&7de vida, una religión."}),


		// KITS GRATUITOS DEL SKYWARS

		ESCUDERO(KitKind.FREE, "Escudero", "K10", Material.WOOD_SWORD,
				new KitContents(KitContents.KIT_ESCUDERO_CONTENTS), 
				new String[] {"", ""}),

		LADRON(KitKind.FREE, "Ladrón", "K11", Material.CHAINMAIL_BOOTS,
				new KitContents(KitContents.KIT_LADRON_CONTENTS), 
				new String[] {""}),

		FLECHERO(KitKind.FREE, "Flechero", "K12", Material.BOW, 
				new KitContents(KitContents.KIT_FLECHERO_CONTENTS), 
				new String[] {""}),

		ALQUIMISTA(KitKind.FREE, "Alquimista", "K13", Material.POTION,
				new KitContents(KitContents.KIT_ALQUIMISTA_CONTENTS), 
				new String[] {""}),

		ORONDO(KitKind.FREE, "Orondo", "K14", Material.PUMPKIN_PIE, 
				new KitContents(KitContents.KIT_ORONDO_CONTENTS), 
				new String[] {""}),
		
		NONE(KitKind.GENERAL, "Ninguno", "&e✯✯✯✯✯✯✯✯✯", "K15", Material.BARRIER, 
				new KitContents(KitContents.KIT_NONE_CONTENTS), 1000, 5,
				new String[] {
						"&8- &7El increíble, magnifico,",
						"&7Épico y legendario",
						"&7ultra Kit vacío!"}),
		;

		private String code;
		private String name;
		private String rarityString;
		private String[] lore;

		private int price = 0;
		private int rarity = 0;
		private int data = 0;

		private Material mat;
		private KitKind Kind;
		private KitContents kitContents;

		SWKitsType(KitKind kind, String name, String code, Material mat, KitContents kitcontents, int rarity, int price,
				String[] lore) {

			this.kitContents = kitcontents;
			this.Kind = kind;

			this.mat = mat;

			this.name = name;
			this.code = code;
			this.lore = lore;
			this.rarity = rarity;
			this.price = price;

		}
		
		SWKitsType(KitKind kind, String name, String rarityString, String code, Material mat, KitContents kitcontents, int rarity, int price,
				String[] lore) {

			this.kitContents = kitcontents;
			this.Kind = kind;

			this.mat = mat;

			this.name = name;
			this.rarityString = rarityString;
			this.code = code;
			this.lore = lore;
			this.rarity = rarity;
			this.price = price;

		}

		SWKitsType(KitKind kind, String name, String rarityString, String code, Material mat, int data, KitContents kitcontents, int rarity,
				int price, String[] lore) {

			this.kitContents = kitcontents;
			this.Kind = kind;

			this.mat = mat;

			this.rarityString = rarityString;
			this.name = name;
			this.code = code;
			this.lore = lore;
			
			this.rarity = rarity;
			this.price = price;
			this.data = data;

		}
		
		SWKitsType(KitKind kind, String name, String code, Material mat, KitContents kitcontents, int price, String[] lore) {

			this.kitContents = kitcontents;
			this.Kind = kind;

			this.mat = mat;

			this.name = name;
			this.code = code;
			this.lore = lore;
			
			this.price = price;

		}

		SWKitsType(KitKind kind, String name, String code, Material mat, KitContents kitcontents, String[] lore) {

			this.kitContents = kitcontents;
			this.Kind = kind;

			this.mat = mat;

			this.name = name;
			this.code = code;
			this.lore = lore;

		}

		public String getName() {
			return name;
		}
		
		public String getRarityString() {
			return rarityString;
		}
		
		public String getCode() {
			return code;
		}

		public int getPrice() {
			return price;
		}

		public int getRarity() {
			return rarity;
		}
		
		public int getData() {
			return data;
		}

		public String[] getLore() {
			return lore;
		}

		public Material getMaterial() {
			return mat;
		}

		public KitKind getKind() {
			return Kind;
		}

		public KitContents getKitContents() {
			return kitContents;
		}

	}

	public static class KitItems {

		private ItemStack helmet;
		private ItemStack chestplate;
		private ItemStack leggings;

		private ItemStack boots;
		private ItemStack mainArticle;
		private ItemStack secondaryArticle;
		private ItemStack tertiaryArticle;
		

		public KitItems(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots,
				ItemStack mainArticle, ItemStack secondaryArticle, ItemStack tertiaryArticle) {

			this.helmet = helmet;
			this.chestplate = chestplate;
			this.leggings = leggings;
			this.boots = boots;

			this.mainArticle = mainArticle;
			this.secondaryArticle = secondaryArticle;
			this.tertiaryArticle = tertiaryArticle;

		}

		public ItemStack getHelmet() {
			return helmet;
		}

		public ItemStack getChestplate() {
			return chestplate;
		}

		public ItemStack getLeggings() {
			return leggings;
		}

		public ItemStack getBoots() {
			return boots;
		}

		public ItemStack getMainArticle() {
			return mainArticle;
		}

		public ItemStack getSecondaryArticle() {
			return secondaryArticle;
		}

		public ItemStack getTertiaryArticle() {
			return tertiaryArticle;
		}
	}

	public static class KitContents {

		private List<KitItems> items = KitContents.KIT_NONE_CONTENTS;

		public KitContents(List<KitItems> items) {

			this.items = items;

		}

		public void equipKit(Player player) {

			for (KitItems items : items) {

				player.getInventory().setHelmet(items.getHelmet());
				player.getInventory().setChestplate(items.getChestplate());
				player.getInventory().setLeggings(items.getLeggings());
				player.getInventory().setBoots(items.getBoots());

				player.getInventory().setItem(0, items.getMainArticle());
				player.getInventory().setItem(1, items.getSecondaryArticle());
				player.getInventory().setItem(2, items.getTertiaryArticle());

				break;

			}
		}

		public final static List<KitItems> KIT_NONE_CONTENTS = Arrays
				.asList(new KitItems(null, null, null, null, null, null, null));

		public final static List<KitItems> KIT_CABALLERO_CONTENTS = Arrays.asList(new KitItems(/* CASCO */
				new ItemBuilder(Material.IRON_HELMET).amount(1)
						.name("&fBlindaje ligero")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nCABALLERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build(),
				/* PECHERA */
				new ItemBuilder(Material.IRON_CHESTPLATE).amount(1)
						.name("&fBlindaje ligero")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nCABALLERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build(),
				/* PANTALONES */
				new ItemBuilder(Material.IRON_LEGGINGS).amount(1)
						.name("&fBlindaje ligero")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nCABALLERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build(),
				/* BOTAS */
				new ItemBuilder(Material.IRON_BOOTS).amount(1)
						.name("&fBlindaje ligero")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nCABALLERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build(),
				/* SLOT 001 */
				new ItemBuilder(Material.IRON_SWORD).amount(1)
						.name("&fEspada noble")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oEspada muy afilada y")
						.lore("&7&ocuidada, se diría que")
						.lore("&7&oestá recien sacada de la").lore("&7&oarmería.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&lDos &f&l| &7equivalen a &cMedio corazon&8&l.")
						.lore("&c&lAtaque&8&l: &4&l&n|||||&c&l&n|||||&e&l&n||")
						.lore("&8&l[&7&nCABALLERO&8&l]").build(),
				/* SLOT 002 */
				new ItemBuilder(Material.CARROT).amount(5).name("&fAlmuerzo")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nCABALLERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				/* SLOT 003 */
				new ItemBuilder(Material.SHIELD).amount(1).build()));

		public final static List<KitItems> KIT_ATLETA_CONTENTS = Arrays.asList(new KitItems(null, null, null,
				new ItemBuilder(Material.LEATHER_BOOTS).amount(1)
						.name("&fCalzado ligero")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oHecho para un atleta olimpico.")
						.lore("&7&oTienen amortiguación para una")
						.lore("&7&omayor velocidad alcanzable.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nATLETA&8&l]")
						.durability((short) 2).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
						.enchant(Enchantment.PROTECTION_FALL, 2).build(),
				new ItemBuilder(Material.POTION).amount(1)
						.name("&fLicor energético")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nATLETA&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.potion(PotionEffectType.SPEED).durability((short) 16418).build(),
				null, null));

		public final static List<KitItems> KIT_ARQUERO_CONTENTS = Arrays.asList(new KitItems(
				new ItemBuilder(Material.IRON_HELMET).amount(1)
						.name("&fBlindaje ligero")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nARQUERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.IRON_CHESTPLATE).amount(1)
						.name("&fBlindaje ligero")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nARQUERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.IRON_LEGGINGS).amount(1)
						.name("&fBlindaje ligero")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nARQUERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.IRON_BOOTS).amount(1)
						.name("&fBlindaje ligero")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nARQUERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.BOW).amount(1)
						.name("&fArco de tirador poder II")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oArma comunmente usada entre")
						.lore("&7&olos guardias de torre, hecho")
						.lore("&7&ocon una excelente calidad.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nARQUERO&8&l]").build(),
				new ItemBuilder(Material.ARROW).amount(50)
						.name("&fFlechas de tirador")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nARQUERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.SHIELD).amount(1).build()));

		public final static List<KitItems> KIT_MAESTRE_CONTENTS = Arrays.asList(new KitItems(
				new ItemBuilder(Material.LEATHER_HELMET).amount(1)
						.name("&fUniforme de alquimia")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nMAESTRE&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build(),
				new ItemBuilder(Material.LEATHER_CHESTPLATE).amount(1)
						.name("&fUniforme de alquimia")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nMAESTRE&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build(),
				new ItemBuilder(Material.LEATHER_LEGGINGS).amount(1)
						.name("&fUniforme de alquimia")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nMAESTRE&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build(),
				new ItemBuilder(Material.LEATHER_BOOTS).amount(1)
						.name("&fUniforme de alquimia")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nMAESTRE&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build(),
				new ItemBuilder(Material.POTION).amount(2)
						.name("&f�?cido mordiente")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oPoción perteneciente a un")
						.lore("&7&omaestre de la alquimia,")
						.lore("&7&osu uso debe ser cauteloso.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nMAESTRE&8&l]")
						.potion(PotionEffectType.POISON).durability((short) 16420).build(),
				new ItemBuilder(Material.POTION).amount(2)
						.name("&fEsporas curativas")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oPoción perteneciente a un")
						.lore("&7&omaestre de la alquimia,")
						.lore("&7&osu uso debe ser cauteloso.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nMAESTRE&8&l]")
						.potion(PotionEffectType.HEAL).durability((short) 16417).build(),
				new ItemBuilder(Material.POTION).amount(2)
						.name("&fLanguidecedora")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oPoción perteneciente a un")
						.lore("&7&omaestre de la alquimia,")
						.lore("&7&osu uso debe ser cauteloso.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nMAESTRE&8&l]")
						.potion(PotionEffectType.SLOW).durability((short) 16426).build()));

		public final static List<KitItems> KIT_MORBIDO_CONTENTS = Arrays.asList(new KitItems(
				new ItemBuilder(Material.LEATHER_HELMET).amount(1)
						.name("&fPrendas cómodas")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nMORBIDO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build(),
				new ItemBuilder(Material.LEATHER_CHESTPLATE).amount(1)
						.name("&fPrendas cómodas")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nMORBIDO&8&l]]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build(),
				new ItemBuilder(Material.LEATHER_LEGGINGS).amount(1)
						.name("&fPrendas cómodas")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nMORBIDO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build(),
				new ItemBuilder(Material.LEATHER_BOOTS).amount(1)
						.name("&fPrendas cómodas")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nMORBIDO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build(),
				new ItemBuilder(Material.COOKED_BEEF).amount(50)	
						.name("&fChuleta de lujo")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nMORBIDO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.GOLDEN_APPLE).amount(1)
						.name("&fCManzana del Eden")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nMORBIDO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				null));

		public final static List<KitItems> KIT_TALADOR_REAL_CONTENTS = Arrays.asList(new KitItems(
				new ItemBuilder(Material.LEATHER_HELMET).amount(1)	
						.name("&fMono de trabajo")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nTALADOR&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build(),
				new ItemBuilder(Material.LEATHER_CHESTPLATE).amount(1)
						.name("&fMono de trabajos")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nTALADOR&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build(),
				new ItemBuilder(Material.LEATHER_LEGGINGS).amount(1)
						.name("&fMono de trabajo")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nTALADOR&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build(),
				new ItemBuilder(Material.LEATHER_BOOTS).amount(1)
						.name("&fMono de trabajo")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nTALADOR&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build(),
				new ItemBuilder(Material.STONE_AXE).amount(1)
						.name("&fHacha enorme")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oHacha tosca y muy resistente.")
						.lore("&7&oSe requiere de una gran")
						.lore("&7&ofuerza para manejarla.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&lDos &f&l| &7equivalen a &cMedio corazon&8&l.")
						.lore("&c&lAtaque&8&l: &4&l&n|||||&c&l&n|||")
						.lore("&8&l[&7&nTALADOR&8&l]")
						.enchant(Enchantment.KNOCKBACK, 4).build(),
				new ItemBuilder(Material.BREAD).amount(10)
						.name("&fAlmuerzo del descanso")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nTALADOR&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				null));

		public final static List<KitItems> KIT_INFANTE_CONTENTS = Arrays.asList(new KitItems(
				new ItemBuilder(Material.GOLD_HELMET).amount(1)
						.name("&fArmadura de lujo")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nINFANTE&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
						.enchant(Enchantment.DURABILITY, 5).build(),
				new ItemBuilder(Material.GOLD_CHESTPLATE).amount(1)
						.name("&fArmadura de lujo")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nINFANTE&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.DURABILITY, 5).build(),
				new ItemBuilder(Material.GOLD_LEGGINGS).amount(1)
						.name("&fArmadura de lujo")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nINFANTE&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
						.enchant(Enchantment.DURABILITY, 5).build(),
				new ItemBuilder(Material.GOLD_BOOTS).amount(1)
						.name("&fArmadura de lujo")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nINFANTE&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.DURABILITY, 5).build(),
				new ItemBuilder(Material.GOLD_SWORD).amount(1)
						.name("&fEspada de lujo")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oEspada de exposición.")
						.lore("&7&oParece que le hayan")
						.lore("&7&oarrancado el soporte.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&lDos &f&l| &7equivalen a &cMedio corazon&8&l.")
						.lore("&c&lAtaque&8&l: &4&l&n|||||&c&l&n|||")
						.lore("&8&l[&7&nINFANTE&8&l]")
						.enchant(Enchantment.DURABILITY, 5)
						.enchant(Enchantment.DAMAGE_ALL, 6).build(),
				new ItemBuilder(Material.GOLDEN_APPLE).amount(5)
						.name("&fVianda de lujo")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nINFANTE&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				null));

		public final static List<KitItems> KIT_SANADOR_CONTENTS = Arrays.asList(new KitItems(
				new ItemBuilder(Material.CHAINMAIL_HELMET).amount(1)
						.name("&fMono de sanitario real")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nSANADOR&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).build(),
				new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).amount(1)
						.name("&fMono de sanitario real")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nSANADOR&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).build(),
				new ItemBuilder(Material.CHAINMAIL_LEGGINGS).amount(1)
						.name("&fMono de sanitario real")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nSANADOR&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).build(),
				new ItemBuilder(Material.CHAINMAIL_BOOTS).amount(1)
						.name("&fMono de sanitario real")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nSANADOR&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).build(),
				new ItemBuilder(Material.POTION).amount(3)
						.name("&fEsporas curativas")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oPoción perteneciente a un")
						.lore("&7&omaestre de la alquimia,")
						.lore("&7&osu uso debe ser cauteloso.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nSANADOR&8&l]")
						.potion(PotionEffectType.HEAL)
						.durability((short) 16417).build(),
				new ItemBuilder(Material.POTION).amount(1)
						.name("&fEstimulante")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oPoción perteneciente a un")
						.lore("&7&omaestre de la alquimia,")
						.lore("&7&osu uso debe ser cauteloso.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nSANADOR&8&l]")
						.potion(PotionEffectType.HEAL).durability((short) 8229).build(),
				new ItemBuilder(Material.MILK_BUCKET).amount(1)
						.name("&fVacuna")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oVacuna universal.")
						.lore("&7&oSe desconocen los")
						.lore("&7&oefectos secundarios.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nSANADOR&8&l]").build()));

		public final static List<KitItems> KIT_ESPIA_CONTENTS = Arrays.asList(new KitItems(
				new ItemBuilder(Material.CHAINMAIL_HELMET).amount(1).
						name("&fCoraza anti tirador")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESPIA&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
						.enchant(Enchantment.PROTECTION_PROJECTILE, 5).build(),
				new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).amount(1)
						.name("&fCoraza anti tirador")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESPIA&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
						.enchant(Enchantment.PROTECTION_PROJECTILE, 5).build(),
				new ItemBuilder(Material.CHAINMAIL_LEGGINGS).amount(1)
						.name("&fCoraza anti tirador")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESPIA&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
						.enchant(Enchantment.PROTECTION_PROJECTILE, 5)
						.build(),
				new ItemBuilder(Material.CHAINMAIL_BOOTS).amount(1)
						.name("&fCoraza anti tirador")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESPIA&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
						.enchant(Enchantment.PROTECTION_PROJECTILE, 5)
						.build(),
				new ItemBuilder(Material.IRON_SWORD).amount(1)
						.name("&fDaga corta")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oUsada por los asesinos")
						.lore("&7&oa sueldo, facil de usar")
						.lore("&7&oy de camuflar con la ropa.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&lDos &f&l| &7equivalen a &cMedio corazon&8&l.")
						.lore("&c&lAtaque&8&l: &4&l&n|||||&c&l&n|||||")
						.lore("&8&l[&7&nESPIA&8&l]").build(),
				new ItemBuilder(Material.POTION).amount(1)
						.name("&fOcultador")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oArtefacto ninja, usado")
						.lore("&7&otambien por los espias e")
						.lore("&7&oinformadores. Se adquiere")
						.lore("&7&oen el mercado negro")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESPIA&8&l]")
						.potion(PotionEffectType.INVISIBILITY)
						.durability((short) 8270).build(),
				new ItemBuilder(Material.MILK_BUCKET).amount(1)
						.name("&fVacuna")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oVacuna universal.")
						.lore("&7&oSe desconocen los")
						.lore("&7&oefectos secundarios.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESPIA&8&l]").build()));

		public final static List<KitItems> KIT_PIROMANO_CONTENTS = Arrays.asList(new KitItems(
				new ItemBuilder(Material.CHAINMAIL_HELMET).amount(1)
						.name("&fMono de extinción")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nPIROMANO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
						.enchant(Enchantment.PROTECTION_FIRE, 2)
						.build(),
				new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).amount(1)
						.name("&fMono de extinción")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nPIROMANO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
						.enchant(Enchantment.PROTECTION_FIRE, 2).build(),
				new ItemBuilder(Material.CHAINMAIL_LEGGINGS).amount(1)
						.name("&fMono de extinción")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nPIROMANO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
						.enchant(Enchantment.PROTECTION_FIRE, 2).build(),
				new ItemBuilder(Material.CHAINMAIL_BOOTS).amount(1)
						.name("&fMono de extinción")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nPIROMANO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
						.enchant(Enchantment.PROTECTION_FIRE, 2).build(),
				new ItemBuilder(Material.FLINT_AND_STEEL).amount(1)
						.name("&fEncendedor profesional")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oParece muy delicado,")
						.lore("&7&oCreado por un artesano.")
						.lore("&7&oUn mal uso y podría explotar.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nPIROMANO&8&l]")
						.enchant(Enchantment.DURABILITY, 5)
						.enchant(Enchantment.FIRE_ASPECT, 1).build(),
				new ItemBuilder(Material.POTION).amount(1)
						.name("&fFlamable")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oArtefacto usado en la")
						.lore("&7&oextinción de fuegos.")
						.lore("&7&oTiene un valor")
						.lore("&7&oincalculable.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nPIROMANO&8&l]")
						.potion(PotionEffectType.FIRE_RESISTANCE)
						.durability((short) 8227).build(),
				new ItemBuilder(Material.SHIELD).amount(1).build()));

		public final static List<KitItems> KIT_ESCUDERO_CONTENTS = Arrays.asList(new KitItems(
				new ItemBuilder(Material.LEATHER_HELMET).amount(1)
				.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESCUDERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.LEATHER_CHESTPLATE).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESCUDERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.LEATHER_LEGGINGS).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESCUDERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.LEATHER_BOOTS).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESCUDERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.WOOD_SWORD).amount(1)
						.name("&fEspada de entrenamiento")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oHerramienta comunmente usada")
						.lore("&7&oen la enseñanza a futuros")
						.lore("&7&ocaballeros y espadachines.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&lDos &f&l| &7equivalen a &cMedio corazon&8&l.")
						.lore("&c&lAtaque&8&l: &4&l&n|||||&c&l&n|")
						.lore("&8&l[&7&nESCUDERO&8&l]").build(),
				new ItemBuilder(Material.BREAD).amount(2)
						.name("&fMerienda")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESCUDERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				null));

		public final static List<KitItems> KIT_LADRON_CONTENTS = Arrays
				.asList(new KitItems(null,
						new ItemBuilder(Material.LEATHER_CHESTPLATE)
								.amount(1).name("&fRopaje ligero")
								.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
								.lore("&8&l[&7&nLADRON&8&l]")
								.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
						null,
						new ItemBuilder(Material.CHAINMAIL_BOOTS).amount(1)
								.name("&fBotas ágiles")
								.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
								.lore("&7&oTípicas botas de ladrón,")
								.lore("&7&ohechas por ellos mismos,")
								.lore("&7&oideales para salir corriendo.")
								.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
								.lore("&8&l[&7&nLADRON&8&l]")
								.enchant(Enchantment.PROTECTION_FALL, 1).build(),
						null, null, null));

		public final static List<KitItems> KIT_FLECHERO_CONTENTS = Arrays.asList(new KitItems(
				new ItemBuilder(Material.LEATHER_HELMET).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESCUDERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.LEATHER_CHESTPLATE).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESCUDERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.LEATHER_LEGGINGS).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESCUDERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.LEATHER_BOOTS).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nESCUDERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.BOW).amount(1)
						.name("&fArco de entrenamiento")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oHerramienta comunmente usada")
						.lore("&7&oen la enseñanza a futuros")
						.lore("&7&oarqueros y saeteros.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nFLECHERO&8&l]").build(),
				new ItemBuilder(Material.ARROW).amount(10)
						.name("&fFlechas huecas")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nFLECHERO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.SHIELD).amount(1).build()));

		public final static List<KitItems> KIT_ALQUIMISTA_CONTENTS = Arrays.asList(new KitItems(
				new ItemBuilder(Material.LEATHER_HELMET).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nALQUIMISTA&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.LEATHER_CHESTPLATE).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nALQUIMISTA&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.LEATHER_LEGGINGS).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nALQUIMISTA&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.LEATHER_BOOTS).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nALQUIMISTA&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.POTION).amount(1)
						.name("&f�?cido corrosivo")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&oPoción perteneciente a un")
						.lore("&7&omaestre de la alquimia,")
						.lore("&7&osu uso debe ser cauteloso.")
						.lore("&7&oincalculable.")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&7&osu uso debe ser cauteloso.")
						.potion(PotionEffectType.POISON).build(),
				null, null));

		public final static List<KitItems> KIT_ORONDO_CONTENTS = Arrays.asList(new KitItems(
				new ItemBuilder(Material.LEATHER_HELMET).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nORONDO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.LEATHER_CHESTPLATE).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nORONDO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.LEATHER_LEGGINGS).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nORONDO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.LEATHER_BOOTS).amount(1)
						.name("&fRopaje de baja estofa")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nORONDO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				new ItemBuilder(Material.COOKED_BEEF).amount(50)
						.name("&fÚltima cena")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.")
						.lore("&8&l[&7&nORONDO&8&l]")
						.lore("&8&l&n.-..-..-..-..-..-..-..-..-..-..-.").build(),
				null, null));

	}
}
