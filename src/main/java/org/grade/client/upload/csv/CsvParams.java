package org.grade.client.upload.csv;

import static java.util.UUID.*;
import static javax.ws.rs.client.Entity.*;
import static javax.ws.rs.core.MediaType.*;
import static org.glassfish.jersey.media.multipart.ContentDisposition.*;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.SneakyThrows;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.grade.client.upload.UploadType;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@EqualsAndHashCode(callSuper=true)
public class CsvParams extends UploadType.Private {

	static final String cpart_name = "content";
	static final String ipart_name = "info";
	static final MediaType media;
	
	static {


		Map<String,String> params = new HashMap<>();
		params.put("boundary", randomUUID().toString());
		
		media = new MediaType("multipart","form-data",params); 
	}
	
	public CsvParams() {
		
		super("csv");
	
	}
	
	@JsonProperty
	private char delimiter = ',';
	
	@NonNull @JsonProperty
	private String encoding = Charset.defaultCharset().name();
	
	@JsonProperty
	private char quote = '"';
	
	
	@SneakyThrows
	@Override
	public Entity<?> bodyWith(InputStream content) {
		
		
		ContentDisposition content_part = type("form-data;name=\""+cpart_name+"\"").build();
		ContentDisposition info_part = type("form-data;name=\""+ipart_name+"\"").build();
		
		BodyPart cpart = new BodyPart(content,new MediaType("text","csv")).contentDisposition(content_part);
		BodyPart ipart = (new BodyPart(this, APPLICATION_JSON_TYPE)).contentDisposition(info_part);
		
		@Cleanup MultiPart multipart = new MultiPart().bodyPart(cpart).bodyPart(ipart);
		
		
		return entity(multipart,compressed(media));
		
		
		
	}
	
	
}
