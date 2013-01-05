package rs.deibogus.client.interfacebuilder;


/**
 * Builder Design Pattern
 * "Abstract Builder"
 * @author bfurtado, durval
 *
 */
public abstract class PageBuilder {

	protected Page page;
	
	public Page getPage() { 
		return page; 
	}
	
	public void createNewPage() { 
		page = new Page(); 
	}
	
	abstract public void buildStructure();
	abstract public void buildHeader();
	abstract public void buildMain();
	abstract public void buildFooter();

	abstract public void destructStructure();
	abstract public void destructHeader();
	abstract void destructMain();
	abstract void destructFooter();

	
	
}
