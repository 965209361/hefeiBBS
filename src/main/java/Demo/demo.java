package Demo;

public class demo {
    public static void main(String[] args) {
        String str="insert into hefei_bbs(listUrl,title,content,fbsj,comment,commentCount) values('http://bbs.hefei.cc/thread-18105538-1-1.html','致刘平平一封信','   曾几何时，我梦到了你，你姗姗来迟，一封彩色的信件给我带来了希望，夜幕降临时想起你的每一个容颜，我淘醉于你那纯净的美丽，我幻想着回到你的身边，把你一揽怀抱，我要让你一览众山小。曾几何时，我每天总是想起你，渴望回到你的身边，那是95年的年代，我总是写给你的信贴，你像一个美丽的天使飞来飞去，让我的心也不着边际。          时光在飞逝，20个春秋轮回，你却不知去向，生活让我们各自延续，每一个美丽的片段，只是在夜深之时我依然想起曾经的信函。         生活是如此美丽，你依然是那个曾经的天使，我的平平，我的梦幻 本帖来自合肥论坛Android客户端 0 0 回 复 请登录后再回复！ 快速回复 神回复','更新于 2018-03-31 17:15','空',0),('http://bbs.hefei.cc/thread-18106255-1-1.html','太丢人！奔驰女司机拒检撒泼踹民警，喊话挑衅：眼睛别放电，我不会喜欢你的...','本帖最后由 有一言 于 2018-4-3 14:20 编辑 近日，宣城市交警在沪渝高速皖浙收费站设卡例行检查过往车辆，一辆黑色奔驰轿车的女性驾驶人姚某因未带驾驶证、行驶证被执勤交警查获。  现场视频（来源：看看新闻 ）   但是，姚某完全不配合民警的检查，不管交警怎么劝说，这名驾驶人就是不下车，仍然稳坐在车内。 “请你马上下车，你听见了吗？” 见驾驶人一直不配合，交警试图强行让其下车，结果姚某开始撒泼脚踹交警。交警无奈之下，只好拿出催泪喷射剂。  “我再警告你三遍，请你马上下车！”  “有意思吗？你们有意思吗？？”   拗不过交警，姚某极不情愿地从车上下来，随后，民警准备依法对车辆进行暂扣，但姚某仍然拒绝交出车钥匙，并且再度推搡交警，甚至还出言不逊。 “打死我算了，打死我算了！交警就是让人看着讨厌，不要脸！” 随后，交警拨打110，请求广德县开发区派出所前来协助执法，派出所民警赶到后，驾驶人这才不情愿地交出了车钥匙。 可就在这时，姚某又开始喊话交警：“眼睛别放电，我不会喜欢你的！”   最终，姚某人因触犯道路交通安全法和涉嫌妨碍执行公务被罚款200元记3分，并被行政拘留9日。 0 0 回 复 请登录后再回复！ 快速回复 神回复','更新于 2018-04-04 16:03','[1 楼:太搞笑了。 本帖来自合肥论坛Android客户端\t||, 2 楼:乖乖停下，回家拿证交罚款走人，下次记得带就行了。犟有啥用？9天\t||, 3 楼:我碰到过出门忘带钥匙的，也常见出门忘带钱包的。出门忘带脑子的我还第一次见 点评 蕤蕤七夕  这次长见识了吧而且还是任性又自恋的女司机  详情 引用 发表于 2018-4-4 10:34\t||, 4 楼:真把自己当根葱了\t||, 5 楼:请注意：人家开的是奔驰，奔驰，再说一遍，奔驰\t||, 6 楼:\t||, 7 楼:\t||, 8 楼:\t||, 9 楼:\t||]',10);";
        System.out.println(str.substring(str.length()-1,str.length()).replaceAll("[,;]",";"));
    }
}
