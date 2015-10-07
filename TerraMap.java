public class TerraMap {
	public int[] map;
	
	private float jag;
	
	public TerraMap(int j, int s) {
		map = new int[800];
		
		for(int x = 0; x < map.length; x++) {
			map[x] = 200;
		}
		
		jag = (10/((float)j));
		
		gen(0,799);
		
		smooth(s);
	}
	
	public void gen(int start, int end) {
		if(end - start <= 1) {
			return;
		}
		
		int change = (int)((Math.random() * 2 - 1)*((end - start)/jag));
		int mid = (int)((start + end)/2);		
		
		//System.out.println("Map start: " + start + " Map end: " + end + " Map[mid]: " + map[mid]);
		
		map[mid] = (map[start] + map[end])/2;
		
		map[mid] += change;
		
		if(map[mid] < 0) map[mid] = 0;
		
		gen(start, mid);
		gen(mid, end);
	}
	
	public void smooth(int s) {
		for(int i = 0; i < s; i++)
		{
			for(int x = 1; x < map.length - 1; x++) {
				map[x] = (int)((map[x-1]+map[x+1])/2);
			}
		}
	}
	
	public void destroy(int x, int y, int rad) {
		for(int xPos = -rad; xPos < rad; xPos++) {
			double yPos = -(Math.sqrt(Math.pow((double)rad,2) - Math.pow((double)xPos,2)));
			//System.out.println(yPos);
			if(y + (int)yPos < map[x + xPos]) {
				if(y - (int)yPos <= map[x + xPos]) {
					//System.out.println(xPos);
					map[x + xPos] += (int)yPos*2;
				} else {
					map[x + xPos] = y + (int)yPos;
				}
			}
		}
	}
}