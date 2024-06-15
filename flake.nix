{
  inputs = {
    utils.url = "github:numtide/flake-utils";
  };
  outputs = { self, nixpkgs, utils }: utils.lib.eachDefaultSystem (system:
    let
      sootup-pkg = {
        lib,
        jre,
        fetchFromGitHub,
        makeWrapper,
        maven
      }: maven.buildMavenPackage rec {
        pname = "SootUp";
        version = "1.3.0";
        src = fetchFromGitHub {
          owner = "secure-software-engineering";
          repo = "${pname}";
          rev = "v${version}";
          hash = "sha256-dBDKpZXMJiUWdLBxAWGdaamX+NlYllIvnmp5QolYe2Q=";
        };

        mvnParameters = "-DskipTests";
        mvnHash = "sha256-rv6nRgljQxUzyFevbMPv5cry+wkOMRLxpWsb/EdC9FQ=";

        nativeBuildInputs = [ makeWrapper ];

        installPhase = ''
          for dir in sootup.jimple.parser sootup.qilin sootup.callgraph sootup.analysis sootup.java.core sootup.java.sourcecode sootup.java.bytecode sootup.examples sootup.core sootup.report; do
            mkdir -p $out/share/$dir
            install -Dm644 $dir/target/$dir-${version}.jar $out/share/$dir
          done
          #makeWrapper ${jre}/bin/java $out/bin/sootup \
            #--add-flags "-jar $out/share/sootup/sootup.jar"
        '';
      };

      pkgs = nixpkgs.legacyPackages.${system};
      sootup = pkgs.callPackage sootup-pkg {};
    in
    {
      packages = {
        #inherit ctadlsweep;
        inherit sootup;
      };
      #defaultPackage = ctadlsweep;
      devShell = pkgs.mkShell {
        buildInputs = with pkgs; [
          sootup jdk17 maven
        ];
        shellHook = ''
          export CLASSPATH="${sootup}/share"
        '';
      };
    }
  );
}
