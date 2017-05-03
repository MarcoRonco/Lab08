package it.polito.tdp.borders.model;

import java.util.List;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	UndirectedGraph<Country, DefaultEdge> mondo;
	BordersDAO dao = new BordersDAO();
	
	public Model() {
	
		this.mondo= new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class);
	}
	
	public String createGraph(int anno){
		
		String ris = "";
		
		for(Country x : dao.loadAllCountries(anno)){
			mondo.addVertex(x);
		}
		
		for(Border x : dao.getCountryPairs(anno)){
			if(mondo.containsVertex(x.getC1()) && mondo.containsVertex(x.getC2()))
				mondo.addEdge(x.getC1(), x.getC2());
		}
		
		for(Country x : mondo.vertexSet()){
			if(mondo.degreeOf(x)!=0)
			ris+=x.getNome()+" = "+mondo.degreeOf(x)+";\n";
		}
		
		int count = 0;
		ConnectivityInspector<Country, DefaultEdge> c = new ConnectivityInspector<Country, DefaultEdge>(mondo);
		
		List<Set<Country>> listaConnesse = c.connectedSets();
		
		for(Set<Country> x : listaConnesse){
			if(x.size()>1)
				count++;
		}
		
		ris+="\nNumero connessioni separate: "+count;
		
		return ris;
	}

	
}
