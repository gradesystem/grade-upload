package org.grade.client.upload;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public interface Deployment {
	
	String uri();
	
	String endpoint();
	
	@Data
	@RequiredArgsConstructor
	class Default implements Deployment {
		
		@NonNull final String uri;
		@NonNull final String endpoint;
		
	}

}
