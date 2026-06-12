import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { MinimalAppModule } from './minimal-app.module';

platformBrowserDynamic()
  .bootstrapModule(MinimalAppModule)
  .catch(err => console.error(err));
