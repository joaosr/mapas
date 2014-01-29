package br.org.imazon.ecotrack_android;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class AdapterGeoJsonToObject {
	
	public List<Ponto> toObject(JSONObject poligonoJson){
		List<Ponto> poligonoObject = new ArrayList<Ponto>();		
		
		try {
			JSONArray coordinates = poligonoJson.getJSONArray("coordinates").getJSONArray(0).getJSONArray(0);
			
			for(int index = 0;index < coordinates.length();index++){
			   JSONArray pontoJson = coordinates.getJSONArray(index);
			   Ponto ponto = new Ponto();			   			   
			   ponto.setLongitude(pontoJson.getDouble(1));
			   ponto.setLatitude(pontoJson.getDouble(0));
			   
			   poligonoObject.add(ponto);
			   
			}
		
        } catch (JSONException e) {
			
			e.printStackTrace();
			//return getRespostaErrorConversaoJsonDTO(getObjetoResposta());
		}
		
		return poligonoObject;
	}

}
