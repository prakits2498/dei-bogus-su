package rs.deibogus.client.interfacebuilder;


/**
 * Builder Design Pattern
 * "Director"
 * @author bfurtado, durval
 *
 */
public class Interface {
	private PageBuilder pageBuilder;

	public void setPageBuilder(PageBuilder pb) { 
		pageBuilder = pb; 
	}
	
	public Page getPage() { 
		return pageBuilder.getPage(); 
	}

	public void construct() {
		pageBuilder.createNewPage();
		pageBuilder.buildStructure();
		pageBuilder.buildHeader();
		pageBuilder.buildMain();
		pageBuilder.buildFooter();
	}
	
	public void destruct() {
		pageBuilder.destructStructure();
		pageBuilder.destructHeader();
		pageBuilder.destructMain();
		pageBuilder.destructFooter();
	}
	
}
