package org.grade.client.upload;

import static javax.ws.rs.client.Entity.*;

import java.io.InputStream;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public interface UploadType {

	@Data
	@RequiredArgsConstructor
	abstract class Private implements UploadType {
		
		@NonNull
		private final String path;
	
		abstract protected Entity<?> bodyWith(InputStream content);
		
		/////////////////////////////////////////////
		
		protected Variant compressed(MediaType media) {
			return new Variant(media ,(String) null, "gzip");	
		}

	}
	
	class Default extends Private {

		@NonNull
		private final MediaType media; 

		public Default(String path, MediaType media) {
			super(path);
			this.media=media;
		}
		
		public Entity<?> bodyWith(InputStream content) {
			return entity(content,compressed(media));	
		}
				
	}
}
