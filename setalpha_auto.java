public class setalpha_auto {
	private int alphas;
	private int alphae;
	private static ImageView tempimage;
	private static int tempalpha;
	private static boolean mark; // 标志alpha变化方向,true表示alpha需要递增,false表示alpha值需要递减
	private Message msg;
	private static Handler temphandler;
	private final int m_step;

	public setalpha_auto(ImageView image, Long time_circle, int alpha_start, int alpha_end, int step) {
		// 设置image的alpha值在alpha_start与alpha_end之间渐变,每隔time_circle变化一次alpha,
		alphas = alpha_start; // 接入透明变化起始alpha值
		alphae = alpha_end; // 接入透明变化结尾alpha值
		tempimage = image; // 接入目标image
		tempalpha = alphae;
		m_step = step;
		msg = new Message();

		TimerTask task = new TimerTask() { // 计时任务
			public void run() {
				if (tempalpha >= alphae) { /// 判断,若动态alpha高于设置上限,变换alpha变化方向为递减
					mark = false;
					msg.what = 0;
					tempalpha = alphae;
				}
				if (tempalpha <= alphas) { // 判断,若动态alpha低于设置下限,变换alpha变化方向为递增
					mark = true;
					msg.what = 1;
					tempalpha = alphas;
				}

				new Thread(new Runnable() {
					public void run() {
						temphandler.sendEmptyMessage(msg.what);
					}
				}).start();
			}
		};
		temphandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				tempimage.setAlpha(tempalpha); // 设置目标alpha值,int型
				if (msg.what == 0) {
					tempalpha -= m_step;
				} else
					tempalpha += m_step;
				super.handleMessage(msg);
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, (long) 0, time_circle);
	}
}
