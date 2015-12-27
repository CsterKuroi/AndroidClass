package cn.zz.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnFocusChangeListener;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class FoodListView extends ListActivity implements OnItemClickListener{//,OnFocusChangeListener
	ListView listView = null;
	// Button[] buttons={};
	private static final String[] food = { "青椒肥肠","青椒土豆丝","地三鲜","京酱肉丝","宫保鸡丁","鱼香肉丝",
			"麻辣香锅","香菇鸡块","鸡蛋炒虾仁","石锅拌饭","滑蛋牛肉","重庆小面","锅贴","水煮鱼" };
	private static final String[] food1 = { "青椒 肥肠","青椒 土豆","茄子 土豆 青椒","豆腐丝 猪肉 葱","鸡肉 黄瓜 花生",
			"猪肉 木耳 胡萝卜","时令蔬菜 肥牛 鸡肉 丸子","香菇 鸡肉","鸡蛋 虾仁","煎鸡蛋 胡萝卜 蔬菜 肉","鸡蛋 牛肉",
			"面 蔬菜 辣椒","面粉 肉 蔬菜","草鱼 黄豆芽 辣椒 花椒"};
	private static final int[] resId = { R.drawable.food1, R.drawable.food2, R.drawable.food3,
			R.drawable.food4, R.drawable.food5, R.drawable.food6,
			R.drawable.food7, R.drawable.food8, R.drawable.food9,
			R.drawable.food10, R.drawable.food11, R.drawable.food12,
			R.drawable.food13, R.drawable.food14};
	 private static final String [] foodjianjie={"青椒肥肠是一道色香味俱全的汉族名菜，属于鲁菜系。香辣可口。老少皆宜。",
			 "土豆中的蛋白质比大豆还好，最接近动物蛋白。从营养角度看，它的营养价值相当于苹果的3.5倍。",
			 "地三鲜是东北家常菜的代表，用料简单，茄子,土豆，青椒。",
			 "京酱肉丝是一道色香味俱全的汉族名菜，属于东北菜或北京菜。此菜咸甜适中，酱香浓郁，风味独特。选用猪里脊肉为主料，辅以黄酱或甜面酱及其它调味品，用北方特有烹调技法“六爆”之一的“酱爆”烹制而成。",
			 "宫保鸡丁选用鸡肉为主料，佐以花生米、黄瓜、辣椒等辅料烹制而成。 红而不辣、辣而不猛、香辣味浓、肉质滑脆。由于其入口鲜辣，鸡肉的鲜嫩配合花生的香脆。",
			 "以鱼香调味而得名。相传灵感来自老菜泡椒肉丝，主料为猪肉、黑木耳，辅料因做法而各异，但多为胡萝卜、竹笋、辣椒等。猪肉选用三成肥、七成瘦的切丝滑炒，吃起来肉丝质地鲜嫩。",
			 "在平常吃的大锅炒菜中加入肉、海鲜、脆肠、肉嫩，笋片清香，腐竹因为事先炸过，可以吸收各种肉类和海鲜的鲜味，加入本身的香味，混合起来，就成了“一锅香”。",
			 "这是一道云南风味菜肴。主料为鸡和蘑菇，口感鸡肉酥烂，菇香甜美，入口回味无穷。营养丰富，很适合于老年人和青年食用。",
			 "鸡蛋炒虾仁是以鸡蛋和虾仁为主材料的菜品，烹饪方式为“炒”制，口味鲜香可口。营养丰富。",
			 "在石锅锅内放入米饭及菜肴，再烤到锅底有一层锅巴，喷香诱人，石锅拌饭材料并不新奇特别，主要为米饭、肉类、鸡蛋，以及黄豆芽、菌菇类和各式野菜，菜的种类并无一定，采用当季最对味的季节时令蔬菜去调配即可。",
			 "滑蛋牛肉是一道广东的汉族特色名菜，属于粤菜菜系中很有特色的菜式之一，滑蛋牛肉以牛肉为主要材料。属广东家常菜，成菜色泽浅黄，肉质鲜滑，蛋香浓郁。",
			 "小面属于汤面类型，麻辣味型。狭义的小面是指麻辣素面。主料为新鲜小麦粉面条，重庆人称之为“水面”、“水叶子”。",
			 "锅贴是一种著名的汉族小吃，煎烙的馅类小食品，制作精巧，味道可口。根据季节配以不同鲜蔬菜。锅贴底面呈深黄色，酥脆，面皮软韧，馅味香美。",
			 "属于重庆渝北风味，选新鲜生猛活鱼，又充分发挥辣椒御寒、益气养血功效，烹调出来的肉质一点也不会变韧，口感滑嫩，油而不腻。"};
	 private static final String []efood={"黄莲","荞麦","雀肉","豆芽","何首乌","地黄","黄豆","海带","醋"," 红豆 ","半夏","南瓜",
			 "橄榄","板粟","韭菜 ", "鲇鱼","鳗鱼","柿子", "狗肉","鲤鱼","柑橘", "鲤鱼"," 绿豆", "鳖", "鲤鱼", "金针菇",
			 "豆浆","兔肉", "甘草", "麦冬", "荞麦面 ", "富含维生素C的食物", "红枣","黄豆",
			 "梨","柿子","茄子","花生仁 ","石榴 ","香瓜","芹菜","蜂蜜","西红柿","芹菜 ","鸭肉","香瓜","木耳","牛肉","蚕豆","玉米",
			 "地黄 何首乌 白术", "枣", "橘子","木耳", "黄瓜","蚬,蛤,蟹", "牛肉", "豆腐","鳝鱼","黄瓜", "蜂蜜",
			 "糖浆", "白酒", "蜂蜜", "胡萝卜", "酒", "蜂蜜", "竹笋", "皮蛋", "豆腐","韭菜",
			 "钙片","果汁","药物","韭菜","柠檬", "胡萝卜","核桃","啤酒","红薯", "海鲜"};
	 private static final String [] efoodinfo={"猪肉多脂，酸寒滑腻。若中药配方中以黄莲为主时，应忌食猪肉，不然会降低药效，且容易引起腹泻。",
			 "《食疗本草》 记载：荞麦难消，动热风，不宜多食。由于荞麦面气味甘平而寒，猪肝多脂，两者都是不易消化之物，所以不宜同食。",
			 "同食会消化不良。","猪肝中的铜会加速豆芽中的维生素C氧化，失去其营养价值，因此猪肝不宜与豆芽、西红柿、山楂等富含维生素C的食物同食。",
			 "《本草纲目》记载：地黄、何首乌忌一切血、葱、蒜、萝卜，何首乌忌铁器。从药物化学理解之，何首乌所含有机酸中，亦有鞣酸存在。鞣酸遇铁则形成不易溶解之物质，且影响其他有效成分的吸收。而动物学中，皆含铁质，故何首乌亦忌诸血。",
			 "《本草纲目》记载：地黄、何首乌忌诸血、葱、蒜、萝卜，服地黄、何首乌诸血忌食之，云能损阳也。",
			 "同食容易消化不良。","同食会便秘。","《本草纲目》记载：羊肉同醋食伤人心。羊肉大热，醋性甘温，与酒性相近，两物同煮，易生火动血。",
			 "红豆不宜与羊肉同食。","会影响营养成分吸收。","《本草纲目》记载：南瓜不可与羊肉同食，令人气壅。南瓜补中益气，羊肉大热补虚，两补同进，会导致胸闷腹胀等症状。",
			 "同食会引起身体不适。","同食会引起身体不适。","易引起气血不顺，发热动火。","《本草图经》记载：鲇鱼肉不可合牛肝食，令人患风，噎涎。同食会引起身体不适。",
			 "鳗鱼所含的某些生物活性物质，对人体产生一定的不良作用。牛肝营养丰富，所含生物活性物质极为复杂，二者同食更易产生不利于人体的生化反应。",
			 "容易引起中毒。","两者同食，温热助火作用更强，不利健康。","两者同食，温热助火作用更强，不利健康。","柑橘味甘酸、性温，兔肉味酸性冷，两者同食会引起胃肠功能絮乱，导致腹泻。",
			 "二者的性味及营养功能不同，不宜共食。《金匮要略》记载：鲤鱼不可合犬肉食之。《饮膳正要》记载：鲤鱼不可与犬肉同食。","狗肉属于大热，有小毒，绿豆则属于大寒，能解毒，两种东西混在一起吃，容易引起身体不适。",
			 "《饮膳正要》记载：鸭肉不可与鳖肉同食。《本草纲目》记载：鳖肉甘平无毒，鳖甲咸平。鳖性冷，发水病，而鸭肉也属凉性，所以鸭肉不宜与鳖肉同食。","鸡肉甘温，鲤鱼甘平。鸡肉补中助阳，鲤鱼下气利水，性味不反但功能相乘。因此鸡肉不可与鲤鱼一起食用。",
			 "同食会引起心痛。","豆浆中含有胰蛋白酶，与鸡蛋清中的卵松蛋白相结合，会造成营养成分的损失，降低二者的营养价值。","《本草纲目》中说：鸡蛋同兔肉食成泄痢，因此两者同食容易引起腹泻。","同食会中毒。",
			 "麦冬养阴生津，鲤鱼利水消肿，两者功能不协调，同食损害健康。","食疗本草 记载：荞麦难消，动热风，不宜多食。由于荞麦面气味甘平而寒，黄鱼多脂，两者都是不易消化之物，所以不宜同食。","大量同食会生成有毒化学物，引起中毒。","同食会中毒。",
			 "同食会影响消化。","《名医别录》记载：梨性冷利，多食损人，因此梨性寒冷，蟹亦冷利，两者同食，伤肠胃。","《饮膳正要》记载：柿梨不可与蟹同食。柿中含鞣酸，蟹内富含蛋白，二者相遇，凝固为鞣酸蛋白，不易消化且妨碍消化功能。",
			 "蟹肉性寒，茄子甘寒滑利，这两者的食物药性同属寒性。一起吃用，肠胃会不舒服，严重的可能导致腹泻，特别是脾胃虚寒的人更应忌食。","花生仁性味甘平，与螃蟹同食食用易导致腹泻。","刺激肠胃，出现腹痛、恶心、呕吐等症状。",
			 "易导致腹泻。","同食会引起蛋白质的吸收受阻。","同食会中毒。","易引起腹泻。","同食易引起腹泻。","猪、兔、鸭之肉都属寒性，而鳖也属寒性，所以不宜配食。","田螺大寒，香瓜冷利，并有轻度导泻作用，二者皆属凉性，同食有损肠胃。所以食用田螺后不宜马上吃香瓜，更不宜同食。",
			 "寒性的田螺，遇上滑利的木耳，不利于消化，所以二者不宜同食。","不易消化，容易腹胀。","同食会肠绞痛。","同食容易中毒。","同食会影响营养成分的吸收。","同食会引起腹泻。","《本草纲目》、《饮膳正要》记载：有苍白术，忌食菘、葫荽、蒜，即苍术、白术不宜与白菜，大蒜等同食。",
			 "辛热助火。","萝卜会产生一种抗甲状腺的物质硫氰酸，如果同时食用大量的橘子、苹果、葡萄等水果，水果中的类黄酮物质在肠道经细菌分解后就会转化为抑制甲状腺作用的硫氰酸，进而诱发甲状腺肿大。","易诱发过敏性皮炎。","黄瓜中含有维生素C分解酶，两者同食，芹菜中的维生素C将会被分解破坏，降低营养价值。",
			 "蚬、蛤、蟹等体内皆含维生素B1分解酶，若与芹菜同食，可将其中的维生素B1破坏，降低营养价值。","《本草纲目》记载：牛肉合韭薤食，令人热病。牛肉甘温，补气助火；韭菜、薤、生姜等食物皆大辛大温之品。如将牛肉配以韭菜、薤、生姜等大辛大温的食物烹调食用，容易上火。",
			 "菠菜中的草酸与豆腐中的钙形成草酸钙，使钙质无法吸收。","同食易导致腹泻。","黄瓜中含有维生素C分解酶，与菠菜同食，会破坏菠菜中的维生素C。","同食易导致腹泻。","同食会引起中毒。","同食会感觉胸闷、气短。","同食伤眼睛，引起眼睛不适。",
			 "胡萝卜素在胡萝卜中含量较高，其中最具有维生素A生物活性的是β-胡萝卜素，但其在人类肠道中的吸收利用率很低，大约仅为维生素A的六分之一，其他胡萝卜素的吸收率更低， 而且遇酸会分解掉，因此胡萝卜不宜与醋一起食用。","喝酒后在酒精的作用下，人体处于酒精中毒的兴奋期，交感神经系统兴奋，心率加快，血压上升。而浓茶中的主要成分茶碱和咖啡因也可以兴奋人的交感神经系统。此时茶酒结合，可以使交感神经系统更加兴奋，对于有高血压、冠心病的人来说，就有可能使病情加重，甚至可能诱发中风或心肌梗塞。",
			 "豆浆中的蛋白质与蜂蜜容易产生变性沉淀，不能被人体吸收。","红糖甘温，竹笋甘寒，食物药性稍有抵触。竹笋蛋白中含有18种左右的氨基酸，其中的赖氨酸在与糖共同加热的过程中，易形成赖氨酸糖基，对人体不利。","《本草纲目》记载：皮蛋忌红糖，同食发呕。","豆腐能清热散血，下大肠浊气，蜂蜜甘凉滑利，两物同食，容易导致腹泻。",
			 "易导致腹泻。","牛奶中已经含有充足的钙，再与钙同服，不利于钙的吸收。","容易引起消化不良或腹泻。","牛奶中的钙、磷、铁容易和中药中的有机物质发生化学反应,生成难溶并稳定的化合物,使牛奶和药物的有效成分受到破坏。","牛奶与含草酸多的蔬菜混合食用，会影响钙的吸收。","牛奶和酸性水果不能一起吃。牛奶中的蛋白质８０％以上为酪蛋白，如在酸性情况下，酪蛋白易凝集，导致消化不良或腹泻。",
			 "胡萝卜含有丰富的胡萝卜素，与酒精一起进入人体，就会在肝脏中产生毒素，从而损害肝脏功能。","易导致血热，严重会鼻出血。","同食会使胃酸分泌减少，导致胃痉挛等症状，对心脑血管危害大。","同食易患结石。","食用海鲜时饮用大量啤酒会产生过多的尿酸从而引发痛风。尿酸过多便会沉积在关节和软组织中，进而引起关节和组织发。",""};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list);
		
		// listView=(ListView)findViewById(R.id.listview01);
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < resId.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ImageView01", resId[i]);
			map.put("TextView01", food[i]);
			map.put("TextView02", food1[i]);
            lists.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, lists,
				R.layout.list_view_row, new String[] { "ImageView01",
						"TextView01", "TextView02" }, new int[] {
						R.id.ImageView01, R.id.TextView01, R.id.TextView02 });
		setListAdapter(adapter);
        
		listView=this.getListView();
        listView.setOnItemClickListener(this);
		
//          listView=this.getListView();
//          listView.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent(FoodListView.this, FoodInfo.class);
//				intent.putExtra("food1", food1[arg2]);
//				intent.putExtra("resId", resId[arg2]);
//				intent.putExtra("food", food[arg2]);
//				startActivity(intent);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}
//	new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view,
//				int position, long id) {
//			// TODO Auto-generated method stub
//			Intent intent=new Intent(FoodListView.this, FoodInfo.class);
////			List<Map<String, Object>> value=new ArrayList<Map<String, Object>>();
////			
////			Map<String, Object> map=new HashMap<String, Object>();
////			map.put("food1", food[position]);
////			map.put("resId", resId[position]);
////			map.put("food", food[position]);
////			value.add(map);
//			//intent.putCharSequenceArrayListExtra(name, value)
//			//intent.
//			//startActivity(intent);
//			setContentView(R.layout.foodinfo);
//		}
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> data=new ArrayList<Map<String,Object>>();
			for(int j=0;j<efood.length;j++){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put(efood[j], efoodinfo[j]);
			data.add(map);
			}
		Intent intent=new Intent();
		intent.setClass(this, FoodInfo.class);
		intent.putExtra("drawable", resId[position]);
		intent.putExtra("foodname", food[position]);
		intent.putExtra("efoodnema", food1[position]);
		intent.putExtra("foodinfo", foodjianjie[position]);
		//intent.putExtra("data", data);
//		intent.putCharSequenceArrayListExtra(name, value)("efood", data);
		
		startActivity(intent);
	}
}
