package br.org.imazon.ecotrack_android;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IInterface;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;


public class MainActivity extends Activity implements LocationListener {
  private LatLng myLocation;	
  static final LatLng BELEM = new LatLng(-1.638939, -55.777092);
  static final LatLng PARAGOMINAS = new LatLng(-1.645660, -55.777143);
  
  static final LatLng POINT1 = new LatLng(-1.445693,-48.484236);
  static final LatLng POINT2 = new LatLng(-1.445811,-48.483984);
  static final LatLng POINT3 = new LatLng(-1.446106,-48.484062);
  
  private Marker marker1;
  private Marker marker2;
  private Marker marker3;
  
  private Marker myMarker;
  private GoogleMap map;  

  @SuppressLint("NewApi")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    
    map.getProjection().getVisibleRegion();
    map.setMyLocationEnabled(true);
    
    
            
    Log.i("MainActivity", map.getProjection().getVisibleRegion().toString());
    
    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    
    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    
    
    AdapterGeoJsonToObject adapter = new AdapterGeoJsonToObject();
        
    
    PolygonOptions rectOptions = new PolygonOptions();
    
    
    JSONObject poligonoJson;
    
	try {
		poligonoJson = new JSONObject(abrirJson());
		rectOptions = criarPoligono(adapter.toObject(poligonoJson));
	} catch (JSONException e) {		
		e.printStackTrace();
	}
    
	marker1 = map.addMarker(new MarkerOptions().position(POINT1).title("Ponto 1").snippet(""));	
	
	marker2 = map.addMarker(new MarkerOptions().position(POINT2).title("Ponto 2").snippet(""));
	
	marker3 = map.addMarker(new MarkerOptions().position(POINT3).title("Ponto 3").snippet(""));
	
	    
    //Marker hamburg = map.addMarker(new MarkerOptions().position(BELEM).title("Belém").snippet("Belém sempre hot"));
    //Marker kiel = map.addMarker(new MarkerOptions().position(PARAGOMINAS).title("Paragominas").snippet("Paragominas município verde"));
    
    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    
    if(map.getMyLocation()==null){
    	myLocation = new LatLng(location.getLatitude(), location.getLongitude());    	
    	Log.i("MainActivity","não encontrou MyLocation");
    }else{
    	myLocation = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());    	
    }
    
    myMarker = map.addMarker(new MarkerOptions().position(myLocation).title("Me").snippet(""));
    
    
    Polygon poligonoParagominas = map.addPolygon(rectOptions.strokeColor(Color.BLACK).fillColor(Color.TRANSPARENT));  
    CameraUpdate cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(myLocation, 30);
    map.moveCamera(cameraUpdateFactory);
    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);          
    
    
    
	if (location != null) {
		//updateLatitudeLongitudeFirst(location);
	}
    
  }
  
  public String abrirJson(){
	InputStream stream = getResources().openRawResource(R.raw.poligono);
	Scanner scanner = new Scanner(stream);
	
	String str = "";
	while(scanner.hasNextLine()){
		String line = scanner.nextLine();
		str = str + line;
	}
	
	
	
	return str;
  }
  
  
  public PolygonOptions criarPoligono(List<Ponto> poligono){
	  PolygonOptions rectOptions = new PolygonOptions();
	  
	  for(Ponto ponto:poligono){
		  Log.v("myjason", ponto.getLongitude()+", "+ponto.getLatitude());
		  rectOptions.add(new LatLng(ponto.getLongitude(), ponto.getLatitude()));
		 
	  }
	  
	  return rectOptions;
  }
  
  

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
     getMenuInflater().inflate(R.menu.main, menu);
     return true;
   }

   private void updateLatitudeLongitudeFirst(Location location){
		map.clear();

		   MarkerOptions mp = new MarkerOptions();

		   mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

		   mp.title("my position");

		   map.addMarker(mp);

		   CameraUpdate cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(
				    new LatLng(location.getLatitude(), location.getLongitude()), 30);
		   map.moveCamera(cameraUpdateFactory);	  
		   
		   
		   Log.i("MyPosition", location.getLatitude()+", "+location.getLongitude());	   
   }
   
   private void updateLatitudeLongitude(Location location){
		map.clear();

		   MarkerOptions mp = new MarkerOptions();

		   mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

		   mp.title("my position");

		   map.addMarker(mp);

		   
		   CameraUpdate cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(
				    new LatLng(location.getLatitude(), location.getLongitude()), 30);
		   map.moveCamera(cameraUpdateFactory);	    
		   
		   
		   Log.i("MyPosition", location.getLatitude()+", "+location.getLongitude());
   }
   
	@Override
	public void onLocationChanged(Location location) {
		 if(map.getMyLocation()==null){
			 	myLocation = new LatLng(location.getLatitude(), location.getLongitude());
		    	Log.i("MainActivity","não encontrou MyLocation");
		    }else{
		    	myLocation = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());
		    }
		    
		 myMarker.setPosition(myLocation);
		
		CameraUpdate cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(myLocation, 30);
		map.moveCamera(cameraUpdateFactory);		
		//updateLatitudeLongitude(location);
								
		marker1.setSnippet(exibirDistancia(marker1));
		mudarCorMarker(marker1);
				
		marker2.setSnippet(exibirDistancia(marker2));
		mudarCorMarker(marker2);
		
		marker3.setSnippet(exibirDistancia(marker3));
		mudarCorMarker(marker3);
		
	}
	
	private String exibirDistancia(Marker marker){
		
		double distancia = calcularDistancia(map.getMyLocation(),locationMarker(marker));
		return distancia+"";
	}
	
	public void mudarCorMarker(Marker marker){
		
		
		
		double distancia = calcularDistancia(map.getMyLocation(),locationMarker(marker));
		
		if(distancia < 20){
			myMarker.setSnippet("Pode coletar!");
		}else{
			myMarker.setSnippet("Va mais perto!");
		}
	}
	
	public Location locationMarker(Marker marker){
		Location local = new Location("Other location");
		local.setLatitude(marker.getPosition().latitude);
		local.setLongitude(marker.getPosition().longitude);	
		
		return local;
	}
	
	public double calcularDistancia(Location inicio, Location fim){						
		double distancia;		
		
		distancia = inicio.distanceTo(fim);								
				
		return distancia;
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
