package gameClient;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.PrintWriter;

import javax.swing.JOptionPane;

import utils.Point3D;


public class KML_Logger {
	private long gameLong;
	private int stage;
	private StringBuilder info;

	/**
	 * Create a KML file of the given game.
	 * @param stage represents the stage of the given game.
	 * @param time represents the length of the game (30 seconds/ 60 seconds).
	 */
	public KML_Logger(int stage, long time) {
		this.gameLong=time;
		this.stage = stage;
		info = new StringBuilder();
		kmlStart();
	}
	/**
	 * Starts write the KML file.
	 */
	public void kmlStart()
	{
		info.append(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
						"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" +
						"  <Document>\r\n" +
						"    <name>" + "Game stage :"+this.stage + "</name>" +"\r\n"+
						"<Style id=\"check-hide-children\">" + 
						"      <ListStyle>\r\n" + 
						"        <listItemType>checkHideChildren</listItemType>\r\n" + 
						"      </ListStyle>\r\n" + 
						"    </Style>\r\n" + 
						"\r\n" + 
						"    <styleUrl>#check-hide-children</styleUrl>"+
						" <Style id=\"node\">\r\n" +
						"      <IconStyle>\r\n" +
						"        <Icon>\r\n" +
						"          <href>http://maps.google.com/mapfiles/kml/paddle/blu-blank.png</href>\r\n" +
						"        </Icon>\r\n" +
						"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
						"      </IconStyle>\r\n" +
						"    </Style>" +
						" <Style id=\"banana\">\r\n" +
						"      <IconStyle>\r\n" +
						"        <Icon>\r\n" +
						"          <href>banana.png</href>\r\n" +
						"        </Icon>\r\n" +
						"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
						"      </IconStyle>\r\n" +
						"    </Style>" +
						" <Style id=\"apple\">\r\n" +
						"      <IconStyle>\r\n" +
						"        <Icon>\r\n" +
						"          <href>apple.png</href>\r\n" +
						"        </Icon>\r\n" +
						"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
						"      </IconStyle>\r\n" +
						"    </Style>" +
						" <Style id=\"robot\">\r\n" +
						"      <IconStyle>\r\n" +
						"        <Icon>\r\n" +
						"          <href>robot.png</href>\r\n" +
						"        </Icon>\r\n" +
						"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
						"      </IconStyle>\r\n" +
						"    </Style>"+
						"	<Style id=\"edge\">\r\n" + 
						"		<LineStyle>\r\n" + 
						"			<color>ff336699</color>\r\n" + 
						"			<width>4</width>\r\n" + 
						"		</LineStyle>\r\n" + 
						"	</Style>\r\n" 
				);
	}
	/**
	 * Adds the edges of the graph to the KML.		
	 * @param p1 represents the source node location.
	 * @param p2 represents the destination node location.
	 */
	public void addEdge(Point3D p1, Point3D p2) {
		info.append(
				"    <Placemark>\r\n" +
						"      <styleUrl>#" + "edge" + "</styleUrl>\r\n" +
						"      <LineString>\r\n" +
						"        <coordinates>" + p1.x()+","+p1.y()+",0\r\n"+
						p2.x()+","+p2.y()+",0\r\n"+ "</coordinates>\r\n" +
						"</LineString>\r\n"+
						"    </Placemark>\r\n"
				);
	}

	/**
	 * Adds an element to the KML.
	 * @param id represents the type of the element.
	 * @param location represents the location of the element on the map.
	 */
	public void addPlaceMark(String id, Point3D location)
	{
		long time_start=(gameLong-MyGameGUI.arena.getGame().timeToEnd())/1000;
		long time_end=(gameLong-MyGameGUI.arena.getGame().timeToEnd()+150)/1000;
		info.append(
				"    <Placemark>\r\n" +
						"      <TimeSpan>\r\n" +
						"     <begin>"+time_start+"</begin>\r\n" + 
						"        <end>"+(time_end)+"</end>"+
						" </TimeSpan>\r\n" + 
						"      <styleUrl>#" + id + "</styleUrl>\r\n" +
						"      <Point>\r\n" +
						"        <coordinates>" + location.toString() + "</coordinates>\r\n" +
						"      </Point>\r\n" +
						"    </Placemark>\r\n"
				);

	}
	/**
	 * Save KML to a file.
	 */
	public void kmlEnd()
	{
		String[] options = new String[] {"Yes", "No"};
		int ans = JOptionPane.showOptionDialog(null, "Do you want to save as a KML file?:", "KML file",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, options[0]);
		if(ans==0) {
			info.append(
					"  \r\n</Document>\r\n" +
							"</kml>"
					);
			try
			{ 
				String fileName = "data/"+this.stage + ".kml";
				PrintWriter pw = new PrintWriter(new File(fileName));
				pw.write(info.toString());
				pw.close();
System.out.println("file saved");

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}


}
