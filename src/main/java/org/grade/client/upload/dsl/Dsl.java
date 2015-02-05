package org.grade.client.upload.dsl;

import javax.ws.rs.WebApplicationException;

import org.grade.client.upload.Deployment;
import org.grade.client.upload.UploadType;

public class Dsl {

	@FunctionalInterface
	public static interface TypeClause { 
		
		TargetClause with(UploadType type);
		
	}
	
	@FunctionalInterface
	public static interface InfoClause { 
		
		TargetClause with(UploadType type);
		
	}
	
	@FunctionalInterface
	public static interface TargetClause { 
		
		NameClause in(Deployment target); 
	
	}
	
	@FunctionalInterface
	public static interface NameClause { 
		
		void as(String name) throws WebApplicationException; 
		
	}
}
