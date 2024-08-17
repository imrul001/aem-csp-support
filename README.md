# Sample AEM Project for Content Security Policy Demonstration

- Cleanned up Project Structure: Discarded ui.frontend, ui.test modules
- Added a transformer: 
  - ui.apps: rewriter: /apps/cspsupport/config
  - core: com.ihasan.core.rewrite.NonceScriptTransformerFactory
- Enabling SSI on httpd.conf: adding rule in dispatcher/src/conf/httpd.conf
- Putting dynamic UNIQUE_ID in the script tags as nonce: dispatcher/src/conf.d/enabled_vhost/cspsupport_publish.vhost
- Refernce: https://abdulmunim.com/2024/04/securing-aem-scripts-with-csp-nonces-on-dispatcher-cache/