import setuptools

with open("README.md", "r") as fh:
    long_description = fh.read()

setuptools.setup(
    name="ckpm-mood",
    version="0.0.1",
    author="Isaac D. Griffith, Ph.D.",
    author_email="isaacgriffith@gmail.com",
    description="",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://siliconcode.dev/ckpm",
    packages=setuptools.find_packages(),
    classifiers=[
      "Programming Language :: Python :: 3",
      "License :: OSI Approved :: MIT License",
      "Operating System :: OS Independent",
    ],
    python_requires='>=3.7',
)
