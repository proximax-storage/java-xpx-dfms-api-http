package io.proximax.dfms.model.drive.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import io.proximax.dfms.model.drive.DriveContent;

public class DirectoryContent extends BaseContent {

	private final List<DriveContent> children;

	public DirectoryContent(Optional<String> name, List<DriveContent> children) {
		super(name, true);
		this.children = children;
	}

	public DirectoryContent(Optional<String> name, DriveContent... children) {
		this(name, Arrays.asList(children));
	}

	@Override
	public InputStream getInputStream() throws IOException {
		throw new IllegalStateException("Cannot get an input stream for a directory!");
	}

	@Override
	public List<DriveContent> getChildren() {
		return children;
	}

}
