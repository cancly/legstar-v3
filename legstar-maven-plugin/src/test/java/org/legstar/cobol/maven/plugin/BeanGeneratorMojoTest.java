package org.legstar.cobol.maven.plugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoParameter;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@MojoTest
public class BeanGeneratorMojoTest extends GeneratorMojoBaseTest {

	@BeforeEach
	public void setUp() {
		File outputDirectory = new File("target/generated-sources/java");
		clear(outputDirectory);
	}

	@Test
	@InjectMojo(goal = "generate-bean")
	@MojoParameter(name = "packageNamePrefix", value = "legstar.samples")
	public void testMojoNoSource(BeanGeneratorMojo mojo) throws Exception {
		try {
			mojo.execute();
			fail();
		} catch (MojoExecutionException | MojoFailureException e) {
			assertEquals(
					"Missing a source parameter. This should point to a COBOL copybook file or a directory containing COBOL copybook files.",
					e.getMessage());
		}
	}

	@Test
	@InjectMojo(goal = "generate-bean")
	@MojoParameter(name = "packageNamePrefix", value = "legstar.samples")
	@MojoParameter(name = "source", value = "src/test/cobol")
	public void testMojoDirectory(BeanGeneratorMojo mojo) throws Exception {
		mojo.execute();
		assertTrue(new File("target/generated-sources/legstar/samples/flat01/Flat01Record.java").exists());
	}

	@Test
	@InjectMojo(goal = "generate-bean")
	@MojoParameter(name = "packageNamePrefix", value = "legstar.samples")
	@MojoParameter(name = "source", value = "src/test/cobol/FLAT01")
	public void testMojoFile(BeanGeneratorMojo mojo) throws Exception {
		mojo.execute();
		assertTrue(new File("target/generated-sources/legstar/samples/flat01/Flat01Record.java").exists());
	}


    @Test
    @InjectMojo(goal = "generate-bean")
    @MojoParameter(name = "packageNamePrefix", value = "legstar.samples")
    @MojoParameter(name = "source", value = "src/test/cobol/FLAT01")
    @MojoParameter(name = "hostCharsetName", value = "ISO-8859-1")
    @MojoParameter(name = "rightPadCobolAlphanumWithSpaces", value = "false")
    @MojoParameter(name = "truncateHostStringsTrailingSpaces", value = "true")
    public void testEncodingConfiguration(BeanGeneratorMojo mojo) throws Exception {
        mojo.execute();
        String source = java.nio.file.Files.readString(java.nio.file.Path.of(
                "target/generated-sources/legstar/samples/flat01/Flat01Record.java"));
        assertTrue(source.contains("hostCharsetName = \"ISO-8859-1\""));
        assertTrue(source.contains("rightPadCobolAlphanumWithSpaces = false"));
        assertTrue(source.contains("truncateHostStringsTrailingSpaces = true"));
    }

    @Test
    @InjectMojo(goal = "generate-bean")
    @MojoParameter(name = "packageNamePrefix", value = "legstar.samples")
    @MojoParameter(name = "source", value = "src/test/cobol/FLAT01")
    @MojoParameter(name = "validateSetters", value = "false")
    public void testSetterValidationDisabled(BeanGeneratorMojo mojo) throws Exception {
        mojo.execute();
        String source = java.nio.file.Files.readString(java.nio.file.Path.of(
                "target/generated-sources/legstar/samples/flat01/Flat01Record.java"));
        org.junit.jupiter.api.Assertions.assertFalse(source.contains("throw new IllegalArgumentException"));
        assertTrue(source.contains("this."));
        assertTrue(source.contains("@CobolOrder"));
    }

    @Test
    @InjectMojo(goal = "generate-bean")
    @MojoParameter(name = "packageNamePrefix", value = "legstar.samples")
    @MojoParameter(name = "source", value = "src/test/cobol/FLAT01")
    @MojoParameter(name = "validateSetters", value = "true")
    public void testSetterValidationEnabled(BeanGeneratorMojo mojo) throws Exception {
        mojo.execute();
        String source = java.nio.file.Files.readString(java.nio.file.Path.of(
                "target/generated-sources/legstar/samples/flat01/Flat01Record.java"));
        assertTrue(source.contains("throw new IllegalArgumentException"));
    }
}
