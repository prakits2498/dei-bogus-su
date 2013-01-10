package rs.deibogus.client;

import java.util.ArrayList;

import rs.deibogus.shared.Foto;

public class ClientData {

	private static ClientData instance = null;
	private ArrayList<Foto> fotos = new ArrayList<Foto>();

	private ClientData() {
	}
	public static ClientData getInstance() {
		if(instance == null) {
			instance = new ClientData();
		}
		return instance;
	}
	public ArrayList<Foto> getFotos() {
		return fotos;
	}
	public void setFotos(ArrayList<Foto> fotos) {
		this.fotos = fotos;
	}

}
